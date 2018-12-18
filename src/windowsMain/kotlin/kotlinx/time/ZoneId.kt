package kotlinx.time

import kotlinx.cinterop.*
import platform.windows.*
import windowsZones

actual fun getPlatformCurrentTimeZoneId(): String = memScoped {
    val windowsZone = getApiTimeZone() ?: getRegistryTimeZone() ?: return ""
    val region = getApiRegion() ?: getRegistryRegion() ?: ""
    // Use zone map to identify windows zone + region pair.
    return windowsZones[windowsZone]?.getValue(region) ?: ""
}

actual fun getPlatformAvailableTimeZoneIds(): Set<String> {
    val registryZones = listRegistrySubkeys(
        HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Time Zones")
    return registryZones.flatMap { windowsZones[it]?.values ?: emptyList() }.toSet()
}

private fun getApiTimeZone(): String? = memScoped {
    val tzInfo = alloc<_TIME_DYNAMIC_ZONE_INFORMATION>()
    GetDynamicTimeZoneInformation(tzInfo.ptr)
    return tzInfo.TimeZoneKeyName.toKString().takeIf { it.isNotEmpty() }
}

private fun getApiRegion(): String? = memScoped {
    val geoId = GetUserGeoID(GEOCLASS_NATION)
    val size = GetGeoInfoA(geoId, GEO_ISO2, null, 0, 0)
    val buffer = allocArray<CHARVar>(size)
    GetGeoInfoA(geoId, GEO_ISO2, buffer, size, 0)
    return buffer.toKString().takeIf { it.isNotEmpty() }
}

private fun getRegistryTimeZone(): String? {
    return readRegistryValue(
        HKEY_LOCAL_MACHINE,
        "SYSTEM\\CurrentControlSet\\Control\\TimeZoneInformation",
        "TimeZoneKeyName"
    )
}

private fun getRegistryRegion(): String? {
    return readRegistryValue(
        HKEY_CURRENT_USER,
        "Control Panel\\International\\Geo",
        "Name"
    )
}

// https://github.com/GNOME/glib/blob/master/glib/gtimezone.c#L624-L649
private fun readRegistryValue(keyName: HKEY?, subkeyName: String, name: String) = memScoped {
    var result: String? = null
    val key = alloc<HKEYVar>()
    if (RegOpenKeyExA(keyName, subkeyName, 0, KEY_QUERY_VALUE, key.ptr) == ERROR_SUCCESS) {
        val size = alloc<DWORDVar>()
        if (RegQueryValueExA(key.value, name, null, null, null, size.ptr) == ERROR_SUCCESS) {
            val data = allocArray<BYTEVar>(size.value.toInt())
            if (RegQueryValueExA(key.value, name, null, null, data.getPointer(this), size.ptr) == ERROR_SUCCESS) {
                result = data.readBytes(size.value.toInt()).stringFromUtf8()
            }
        }
        RegCloseKey(key.value)
    }
    result
}

// https://docs.microsoft.com/en-us/windows/desktop/sysinfo/enumerating-registry-subkeys
private fun listRegistrySubkeys(keyName: HKEY?, subkeyName: String = "") = memScoped {
    val result = mutableListOf<String>()
    val key = alloc<HKEYVar>()
    if (RegOpenKeyExA(keyName, subkeyName, 0, KEY_READ, key.ptr) == ERROR_SUCCESS) {
        val size = alloc<DWORDVar>()
        if (RegQueryInfoKeyA(
                key.value, null, null, null, null, size.ptr, null, null, null, null, null, null) == ERROR_SUCCESS) {
            val maxSize = size.value.toInt() + 1
            val data = allocArray<CHARVar>(maxSize)
            var index: DWORD = 0u
            while(RegEnumKeyExA(
                    key.value, index++, data.getPointer(this), size.ptr, null, null, null, null) == ERROR_SUCCESS) {
                result.add(data.toKString())
                size.value = maxSize.toUInt()
            }
        }
        RegCloseKey(key.value)
    }
    result
}