// Windows->IANA map. Each Windows zone has one or more territories, which point to a IANA zone.
val windowsZones = mapOf(
    "Dateline Standard Time" to mapOf(
        "ZZ" to "Etc/GMT+12"
    ).withDefault {
        "Etc/GMT+12"
    },
    "UTC-11" to mapOf(
        "AS" to "Pacific/Pago_Pago",
        "NU" to "Pacific/Niue",
        "UM" to "Pacific/Midway",
        "ZZ" to "Etc/GMT+11"
    ).withDefault {
        "Etc/GMT+11"
    },
    "Aleutian Standard Time" to mapOf(
        "US" to "America/Adak"
    ).withDefault {
        "America/Adak"
    },
    "Hawaiian Standard Time" to mapOf(
        "CK" to "Pacific/Rarotonga",
        "PF" to "Pacific/Tahiti",
        "UM" to "Pacific/Johnston",
        "US" to "Pacific/Honolulu",
        "ZZ" to "Etc/GMT+10"
    ).withDefault {
        "Pacific/Honolulu"
    },
    "Marquesas Standard Time" to mapOf(
        "PF" to "Pacific/Marquesas"
    ).withDefault {
        "Pacific/Marquesas"
    },
    "Alaskan Standard Time" to mapOf(
        "US" to "America/Anchorage",
        "US2" to "America/Juneau",
        "US3" to "America/Metlakatla",
        "US4" to "America/Nome",
        "US5" to "America/Sitka",
        "US6" to "America/Yakutat"
    ).withDefault {
        "America/Anchorage"
    },
    "UTC-09" to mapOf(
        "PF" to "Pacific/Gambier",
        "ZZ" to "Etc/GMT+9"
    ).withDefault {
        "Etc/GMT+9"
    },
    "Pacific Standard Time (Mexico)" to mapOf(
        "MX" to "America/Tijuana",
        "MX2" to "America/Santa_Isabel"
    ).withDefault {
        "America/Tijuana"
    },
    "UTC-08" to mapOf(
        "PN" to "Pacific/Pitcairn",
        "ZZ" to "Etc/GMT+8"
    ).withDefault {
        "Etc/GMT+8"
    },
    "Pacific Standard Time" to mapOf(
        "CA" to "America/Vancouver",
        "CA2" to "America/Dawson",
        "CA3" to "America/Whitehorse",
        "US" to "America/Los_Angeles",
        "ZZ" to "PST8PDT"
    ).withDefault {
        "America/Los_Angeles"
    },
    "US Mountain Standard Time" to mapOf(
        "CA" to "America/Dawson_Creek",
        "CA2" to "America/Creston",
        "CA3" to "America/Fort_Nelson",
        "MX" to "America/Hermosillo",
        "US" to "America/Phoenix",
        "ZZ" to "Etc/GMT+7"
    ).withDefault {
        "America/Phoenix"
    },
    "Mountain Standard Time (Mexico)" to mapOf(
        "MX" to "America/Chihuahua",
        "MX2" to "America/Mazatlan"
    ).withDefault {
        "America/Chihuahua"
    },
    "Mountain Standard Time" to mapOf(
        "CA" to "America/Edmonton",
        "CA2" to "America/Cambridge_Bay",
        "CA3" to "America/Inuvik",
        "CA4" to "America/Yellowknife",
        "MX" to "America/Ojinaga",
        "US" to "America/Denver",
        "US2" to "America/Boise",
        "ZZ" to "MST7MDT"
    ).withDefault {
        "America/Denver"
    },
    "Central America Standard Time" to mapOf(
        "BZ" to "America/Belize",
        "CR" to "America/Costa_Rica",
        "EC" to "Pacific/Galapagos",
        "GT" to "America/Guatemala",
        "HN" to "America/Tegucigalpa",
        "NI" to "America/Managua",
        "SV" to "America/El_Salvador",
        "ZZ" to "Etc/GMT+6"
    ).withDefault {
        "America/Guatemala"
    },
    "Central Standard Time" to mapOf(
        "CA" to "America/Winnipeg",
        "CA2" to "America/Rainy_River",
        "CA3" to "America/Rankin_Inlet",
        "CA4" to "America/Resolute",
        "MX" to "America/Matamoros",
        "US" to "America/Chicago",
        "US2" to "America/Indiana/Knox",
        "US3" to "America/Indiana/Tell_City",
        "US4" to "America/Menominee",
        "US5" to "America/North_Dakota/Beulah",
        "US6" to "America/North_Dakota/Center",
        "US7" to "America/North_Dakota/New_Salem",
        "ZZ" to "CST6CDT"
    ).withDefault {
        "America/Chicago"
    },
    "Easter Island Standard Time" to mapOf(
        "CL" to "Pacific/Easter"
    ).withDefault {
        "Pacific/Easter"
    },
    "Central Standard Time (Mexico)" to mapOf(
        "MX" to "America/Mexico_City",
        "MX2" to "America/Bahia_Banderas",
        "MX3" to "America/Merida",
        "MX4" to "America/Monterrey"
    ).withDefault {
        "America/Mexico_City"
    },
    "Canada Central Standard Time" to mapOf(
        "CA" to "America/Regina",
        "CA2" to "America/Swift_Current"
    ).withDefault {
        "America/Regina"
    },
    "SA Pacific Standard Time" to mapOf(
        "BR" to "America/Rio_Branco",
        "BR2" to "America/Eirunepe",
        "CA" to "America/Coral_Harbour",
        "CO" to "America/Bogota",
        "EC" to "America/Guayaquil",
        "JM" to "America/Jamaica",
        "KY" to "America/Cayman",
        "PA" to "America/Panama",
        "PE" to "America/Lima",
        "ZZ" to "Etc/GMT+5"
    ).withDefault {
        "America/Bogota"
    },
    "Eastern Standard Time (Mexico)" to mapOf(
        "MX" to "America/Cancun"
    ).withDefault {
        "America/Cancun"
    },
    "Eastern Standard Time" to mapOf(
        "BS" to "America/Nassau",
        "CA" to "America/Toronto",
        "CA2" to "America/Iqaluit",
        "CA3" to "America/Montreal",
        "CA4" to "America/Nipigon",
        "CA5" to "America/Pangnirtung",
        "CA6" to "America/Thunder_Bay",
        "US" to "America/New_York",
        "US2" to "America/Detroit",
        "US3" to "America/Indiana/Petersburg",
        "US4" to "America/Indiana/Vincennes",
        "US5" to "America/Indiana/Winamac",
        "US6" to "America/Kentucky/Monticello",
        "US7" to "America/Louisville",
        "ZZ" to "EST5EDT"
    ).withDefault {
        "America/New_York"
    },
    "Haiti Standard Time" to mapOf(
        "HT" to "America/Port-au-Prince"
    ).withDefault {
        "America/Port-au-Prince"
    },
    "Cuba Standard Time" to mapOf(
        "CU" to "America/Havana"
    ).withDefault {
        "America/Havana"
    },
    "US Eastern Standard Time" to mapOf(
        "US" to "America/Indianapolis",
        "US2" to "America/Indiana/Marengo",
        "US3" to "America/Indiana/Vevay"
    ).withDefault {
        "America/Indianapolis"
    },
    "Paraguay Standard Time" to mapOf(
        "PY" to "America/Asuncion"
    ).withDefault {
        "America/Asuncion"
    },
    "Atlantic Standard Time" to mapOf(
        "BM" to "Atlantic/Bermuda",
        "CA" to "America/Halifax",
        "CA2" to "America/Glace_Bay",
        "CA3" to "America/Goose_Bay",
        "CA4" to "America/Moncton",
        "GL" to "America/Thule"
    ).withDefault {
        "America/Halifax"
    },
    "Venezuela Standard Time" to mapOf(
        "VE" to "America/Caracas"
    ).withDefault {
        "America/Caracas"
    },
    "Central Brazilian Standard Time" to mapOf(
        "BR" to "America/Cuiaba",
        "BR2" to "America/Campo_Grande"
    ).withDefault {
        "America/Cuiaba"
    },
    "SA Western Standard Time" to mapOf(
        "AG" to "America/Antigua",
        "AI" to "America/Anguilla",
        "AW" to "America/Aruba",
        "BB" to "America/Barbados",
        "BL" to "America/St_Barthelemy",
        "BO" to "America/La_Paz",
        "BQ" to "America/Kralendijk",
        "BR" to "America/Manaus",
        "BR2" to "America/Boa_Vista",
        "BR3" to "America/Porto_Velho",
        "CA" to "America/Blanc-Sablon",
        "CW" to "America/Curacao",
        "DM" to "America/Dominica",
        "DO" to "America/Santo_Domingo",
        "GD" to "America/Grenada",
        "GP" to "America/Guadeloupe",
        "GY" to "America/Guyana",
        "KN" to "America/St_Kitts",
        "LC" to "America/St_Lucia",
        "MF" to "America/Marigot",
        "MQ" to "America/Martinique",
        "MS" to "America/Montserrat",
        "PR" to "America/Puerto_Rico",
        "SX" to "America/Lower_Princes",
        "TT" to "America/Port_of_Spain",
        "VC" to "America/St_Vincent",
        "VG" to "America/Tortola",
        "VI" to "America/St_Thomas",
        "ZZ" to "Etc/GMT+4"
    ).withDefault {
        "America/La_Paz"
    },
    "Pacific SA Standard Time" to mapOf(
        "CL" to "America/Santiago"
    ).withDefault {
        "America/Santiago"
    },
    "Turks And Caicos Standard Time" to mapOf(
        "TC" to "America/Grand_Turk"
    ).withDefault {
        "America/Grand_Turk"
    },
    "Newfoundland Standard Time" to mapOf(
        "CA" to "America/St_Johns"
    ).withDefault {
        "America/St_Johns"
    },
    "Tocantins Standard Time" to mapOf(
        "BR" to "America/Araguaina"
    ).withDefault {
        "America/Araguaina"
    },
    "E. South America Standard Time" to mapOf(
        "BR" to "America/Sao_Paulo"
    ).withDefault {
        "America/Sao_Paulo"
    },
    "SA Eastern Standard Time" to mapOf(
        "AQ" to "Antarctica/Rothera",
        "BR" to "America/Fortaleza",
        "BR2" to "America/Belem",
        "BR3" to "America/Maceio",
        "BR4" to "America/Recife",
        "BR5" to "America/Santarem",
        "FK" to "Atlantic/Stanley",
        "GF" to "America/Cayenne",
        "SR" to "America/Paramaribo",
        "ZZ" to "Etc/GMT+3"
    ).withDefault {
        "America/Cayenne"
    },
    "Argentina Standard Time" to mapOf(
        "AR" to "America/Buenos_Aires",
        "AR2" to "America/Argentina/La_Rioja",
        "AR3" to "America/Argentina/Rio_Gallegos",
        "AR4" to "America/Argentina/Salta",
        "AR5" to "America/Argentina/San_Juan",
        "AR6" to "America/Argentina/San_Luis",
        "AR7" to "America/Argentina/Tucuman",
        "AR8" to "America/Argentina/Ushuaia",
        "AR9" to "America/Catamarca",
        "AR10" to "America/Cordoba",
        "AR11" to "America/Jujuy",
        "AR12" to "America/Mendoza"
    ).withDefault {
        "America/Buenos_Aires"
    },
    "Greenland Standard Time" to mapOf(
        "GL" to "America/Godthab"
    ).withDefault {
        "America/Godthab"
    },
    "Montevideo Standard Time" to mapOf(
        "UY" to "America/Montevideo"
    ).withDefault {
        "America/Montevideo"
    },
    "Magallanes Standard Time" to mapOf(
        "AQ" to "Antarctica/Palmer",
        "CL" to "America/Punta_Arenas"
    ).withDefault {
        "America/Punta_Arenas"
    },
    "Saint Pierre Standard Time" to mapOf(
        "PM" to "America/Miquelon"
    ).withDefault {
        "America/Miquelon"
    },
    "Bahia Standard Time" to mapOf(
        "BR" to "America/Bahia"
    ).withDefault {
        "America/Bahia"
    },
    "UTC-02" to mapOf(
        "BR" to "America/Noronha",
        "GS" to "Atlantic/South_Georgia",
        "ZZ" to "Etc/GMT+2"
    ).withDefault {
        "Etc/GMT+2"
    },
    "Azores Standard Time" to mapOf(
        "GL" to "America/Scoresbysund",
        "PT" to "Atlantic/Azores"
    ).withDefault {
        "Atlantic/Azores"
    },
    "Cape Verde Standard Time" to mapOf(
        "CV" to "Atlantic/Cape_Verde",
        "ZZ" to "Etc/GMT+1"
    ).withDefault {
        "Atlantic/Cape_Verde"
    },
    "UTC" to mapOf(
        "GL" to "America/Danmarkshavn",
        "ZZ" to "Etc/GMT",
        "ZZ2" to "Etc/UTC"
    ).withDefault {
        "Etc/GMT"
    },
    "GMT Standard Time" to mapOf(
        "ES" to "Atlantic/Canary",
        "FO" to "Atlantic/Faeroe",
        "GB" to "Europe/London",
        "GG" to "Europe/Guernsey",
        "IE" to "Europe/Dublin",
        "IM" to "Europe/Isle_of_Man",
        "JE" to "Europe/Jersey",
        "PT" to "Europe/Lisbon",
        "PT2" to "Atlantic/Madeira"
    ).withDefault {
        "Europe/London"
    },
    "Greenwich Standard Time" to mapOf(
        "BF" to "Africa/Ouagadougou",
        "CI" to "Africa/Abidjan",
        "GH" to "Africa/Accra",
        "GM" to "Africa/Banjul",
        "GN" to "Africa/Conakry",
        "GW" to "Africa/Bissau",
        "IS" to "Atlantic/Reykjavik",
        "LR" to "Africa/Monrovia",
        "ML" to "Africa/Bamako",
        "MR" to "Africa/Nouakchott",
        "SH" to "Atlantic/St_Helena",
        "SL" to "Africa/Freetown",
        "SN" to "Africa/Dakar",
        "TG" to "Africa/Lome"
    ).withDefault {
        "Atlantic/Reykjavik"
    },
    "W. Europe Standard Time" to mapOf(
        "AD" to "Europe/Andorra",
        "AT" to "Europe/Vienna",
        "CH" to "Europe/Zurich",
        "DE" to "Europe/Berlin",
        "DE2" to "Europe/Busingen",
        "GI" to "Europe/Gibraltar",
        "IT" to "Europe/Rome",
        "LI" to "Europe/Vaduz",
        "LU" to "Europe/Luxembourg",
        "MC" to "Europe/Monaco",
        "MT" to "Europe/Malta",
        "NL" to "Europe/Amsterdam",
        "NO" to "Europe/Oslo",
        "SE" to "Europe/Stockholm",
        "SJ" to "Arctic/Longyearbyen",
        "SM" to "Europe/San_Marino",
        "VA" to "Europe/Vatican"
    ).withDefault {
        "Europe/Berlin"
    },
    "Central Europe Standard Time" to mapOf(
        "AL" to "Europe/Tirane",
        "CZ" to "Europe/Prague",
        "HU" to "Europe/Budapest",
        "ME" to "Europe/Podgorica",
        "RS" to "Europe/Belgrade",
        "SI" to "Europe/Ljubljana",
        "SK" to "Europe/Bratislava"
    ).withDefault {
        "Europe/Budapest"
    },
    "Romance Standard Time" to mapOf(
        "BE" to "Europe/Brussels",
        "DK" to "Europe/Copenhagen",
        "ES" to "Europe/Madrid",
        "ES2" to "Africa/Ceuta",
        "FR" to "Europe/Paris"
    ).withDefault {
        "Europe/Paris"
    },
    "Morocco Standard Time" to mapOf(
        "EH" to "Africa/El_Aaiun",
        "MA" to "Africa/Casablanca"
    ).withDefault {
        "Africa/Casablanca"
    },
    "Sao Tome Standard Time" to mapOf(
        "ST" to "Africa/Sao_Tome"
    ).withDefault {
        "Africa/Sao_Tome"
    },
    "Central European Standard Time" to mapOf(
        "BA" to "Europe/Sarajevo",
        "HR" to "Europe/Zagreb",
        "MK" to "Europe/Skopje",
        "PL" to "Europe/Warsaw"
    ).withDefault {
        "Europe/Warsaw"
    },
    "W. Central Africa Standard Time" to mapOf(
        "AO" to "Africa/Luanda",
        "BJ" to "Africa/Porto-Novo",
        "CD" to "Africa/Kinshasa",
        "CF" to "Africa/Bangui",
        "CG" to "Africa/Brazzaville",
        "CM" to "Africa/Douala",
        "DZ" to "Africa/Algiers",
        "GA" to "Africa/Libreville",
        "GQ" to "Africa/Malabo",
        "NE" to "Africa/Niamey",
        "NG" to "Africa/Lagos",
        "TD" to "Africa/Ndjamena",
        "TN" to "Africa/Tunis",
        "ZZ" to "Etc/GMT-1"
    ).withDefault {
        "Africa/Lagos"
    },
    "Jordan Standard Time" to mapOf(
        "JO" to "Asia/Amman"
    ).withDefault {
        "Asia/Amman"
    },
    "GTB Standard Time" to mapOf(
        "CY" to "Asia/Famagusta",
        "CY2" to "Asia/Nicosia",
        "GR" to "Europe/Athens",
        "RO" to "Europe/Bucharest"
    ).withDefault {
        "Europe/Bucharest"
    },
    "Middle East Standard Time" to mapOf(
        "LB" to "Asia/Beirut"
    ).withDefault {
        "Asia/Beirut"
    },
    "Egypt Standard Time" to mapOf(
        "EG" to "Africa/Cairo"
    ).withDefault {
        "Africa/Cairo"
    },
    "E. Europe Standard Time" to mapOf(
        "MD" to "Europe/Chisinau"
    ).withDefault {
        "Europe/Chisinau"
    },
    "Syria Standard Time" to mapOf(
        "SY" to "Asia/Damascus"
    ).withDefault {
        "Asia/Damascus"
    },
    "West Bank Standard Time" to mapOf(
        "PS" to "Asia/Hebron",
        "PS2" to "Asia/Gaza"
    ).withDefault {
        "Asia/Hebron"
    },
    "South Africa Standard Time" to mapOf(
        "BI" to "Africa/Bujumbura",
        "BW" to "Africa/Gaborone",
        "CD" to "Africa/Lubumbashi",
        "LS" to "Africa/Maseru",
        "MW" to "Africa/Blantyre",
        "MZ" to "Africa/Maputo",
        "RW" to "Africa/Kigali",
        "SZ" to "Africa/Mbabane",
        "ZA" to "Africa/Johannesburg",
        "ZM" to "Africa/Lusaka",
        "ZW" to "Africa/Harare",
        "ZZ" to "Etc/GMT-2"
    ).withDefault {
        "Africa/Johannesburg"
    },
    "FLE Standard Time" to mapOf(
        "AX" to "Europe/Mariehamn",
        "BG" to "Europe/Sofia",
        "EE" to "Europe/Tallinn",
        "FI" to "Europe/Helsinki",
        "LT" to "Europe/Vilnius",
        "LV" to "Europe/Riga",
        "UA" to "Europe/Kiev",
        "UA2" to "Europe/Uzhgorod",
        "UA3" to "Europe/Zaporozhye"
    ).withDefault {
        "Europe/Kiev"
    },
    "Israel Standard Time" to mapOf(
        "IL" to "Asia/Jerusalem"
    ).withDefault {
        "Asia/Jerusalem"
    },
    "Kaliningrad Standard Time" to mapOf(
        "RU" to "Europe/Kaliningrad"
    ).withDefault {
        "Europe/Kaliningrad"
    },
    "Sudan Standard Time" to mapOf(
        "SD" to "Africa/Khartoum"
    ).withDefault {
        "Africa/Khartoum"
    },
    "Libya Standard Time" to mapOf(
        "LY" to "Africa/Tripoli"
    ).withDefault {
        "Africa/Tripoli"
    },
    "Namibia Standard Time" to mapOf(
        "NA" to "Africa/Windhoek"
    ).withDefault {
        "Africa/Windhoek"
    },
    "Arabic Standard Time" to mapOf(
        "IQ" to "Asia/Baghdad"
    ).withDefault {
        "Asia/Baghdad"
    },
    "Turkey Standard Time" to mapOf(
        "TR" to "Europe/Istanbul"
    ).withDefault {
        "Europe/Istanbul"
    },
    "Arab Standard Time" to mapOf(
        "BH" to "Asia/Bahrain",
        "KW" to "Asia/Kuwait",
        "QA" to "Asia/Qatar",
        "SA" to "Asia/Riyadh",
        "YE" to "Asia/Aden"
    ).withDefault {
        "Asia/Riyadh"
    },
    "Belarus Standard Time" to mapOf(
        "BY" to "Europe/Minsk"
    ).withDefault {
        "Europe/Minsk"
    },
    "Russian Standard Time" to mapOf(
        "RU" to "Europe/Moscow",
        "RU2" to "Europe/Kirov",
        "RU3" to "Europe/Volgograd",
        "UA" to "Europe/Simferopol"
    ).withDefault {
        "Europe/Moscow"
    },
    "E. Africa Standard Time" to mapOf(
        "AQ" to "Antarctica/Syowa",
        "DJ" to "Africa/Djibouti",
        "ER" to "Africa/Asmera",
        "ET" to "Africa/Addis_Ababa",
        "KE" to "Africa/Nairobi",
        "KM" to "Indian/Comoro",
        "MG" to "Indian/Antananarivo",
        "SO" to "Africa/Mogadishu",
        "SS" to "Africa/Juba",
        "TZ" to "Africa/Dar_es_Salaam",
        "UG" to "Africa/Kampala",
        "YT" to "Indian/Mayotte",
        "ZZ" to "Etc/GMT-3"
    ).withDefault {
        "Africa/Nairobi"
    },
    "Iran Standard Time" to mapOf(
        "IR" to "Asia/Tehran"
    ).withDefault {
        "Asia/Tehran"
    },
    "Arabian Standard Time" to mapOf(
        "AE" to "Asia/Dubai",
        "OM" to "Asia/Muscat",
        "ZZ" to "Etc/GMT-4"
    ).withDefault {
        "Asia/Dubai"
    },
    "Astrakhan Standard Time" to mapOf(
        "RU" to "Europe/Astrakhan",
        "RU2" to "Europe/Ulyanovsk"
    ).withDefault {
        "Europe/Astrakhan"
    },
    "Azerbaijan Standard Time" to mapOf(
        "AZ" to "Asia/Baku"
    ).withDefault {
        "Asia/Baku"
    },
    "Russia Time Zone 3" to mapOf(
        "RU" to "Europe/Samara"
    ).withDefault {
        "Europe/Samara"
    },
    "Mauritius Standard Time" to mapOf(
        "MU" to "Indian/Mauritius",
        "RE" to "Indian/Reunion",
        "SC" to "Indian/Mahe"
    ).withDefault {
        "Indian/Mauritius"
    },
    "Saratov Standard Time" to mapOf(
        "RU" to "Europe/Saratov"
    ).withDefault {
        "Europe/Saratov"
    },
    "Georgian Standard Time" to mapOf(
        "GE" to "Asia/Tbilisi"
    ).withDefault {
        "Asia/Tbilisi"
    },
    "Caucasus Standard Time" to mapOf(
        "AM" to "Asia/Yerevan"
    ).withDefault {
        "Asia/Yerevan"
    },
    "Afghanistan Standard Time" to mapOf(
        "AF" to "Asia/Kabul"
    ).withDefault {
        "Asia/Kabul"
    },
    "West Asia Standard Time" to mapOf(
        "AQ" to "Antarctica/Mawson",
        "KZ" to "Asia/Oral",
        "KZ2" to "Asia/Aqtau",
        "KZ3" to "Asia/Aqtobe",
        "KZ4" to "Asia/Atyrau",
        "MV" to "Indian/Maldives",
        "TF" to "Indian/Kerguelen",
        "TJ" to "Asia/Dushanbe",
        "TM" to "Asia/Ashgabat",
        "UZ" to "Asia/Tashkent",
        "UZ2" to "Asia/Samarkand",
        "ZZ" to "Etc/GMT-5"
    ).withDefault {
        "Asia/Tashkent"
    },
    "Ekaterinburg Standard Time" to mapOf(
        "RU" to "Asia/Yekaterinburg"
    ).withDefault {
        "Asia/Yekaterinburg"
    },
    "Pakistan Standard Time" to mapOf(
        "PK" to "Asia/Karachi"
    ).withDefault {
        "Asia/Karachi"
    },
    "India Standard Time" to mapOf(
        "IN" to "Asia/Calcutta"
    ).withDefault {
        "Asia/Calcutta"
    },
    "Sri Lanka Standard Time" to mapOf(
        "LK" to "Asia/Colombo"
    ).withDefault {
        "Asia/Colombo"
    },
    "Nepal Standard Time" to mapOf(
        "NP" to "Asia/Katmandu"
    ).withDefault {
        "Asia/Katmandu"
    },
    "Central Asia Standard Time" to mapOf(
        "AQ" to "Antarctica/Vostok",
        "CN" to "Asia/Urumqi",
        "IO" to "Indian/Chagos",
        "KG" to "Asia/Bishkek",
        "KZ" to "Asia/Almaty",
        "KZ2" to "Asia/Qyzylorda",
        "ZZ" to "Etc/GMT-6"
    ).withDefault {
        "Asia/Almaty"
    },
    "Bangladesh Standard Time" to mapOf(
        "BD" to "Asia/Dhaka",
        "BT" to "Asia/Thimphu"
    ).withDefault {
        "Asia/Dhaka"
    },
    "Omsk Standard Time" to mapOf(
        "RU" to "Asia/Omsk"
    ).withDefault {
        "Asia/Omsk"
    },
    "Myanmar Standard Time" to mapOf(
        "CC" to "Indian/Cocos",
        "MM" to "Asia/Rangoon"
    ).withDefault {
        "Asia/Rangoon"
    },
    "SE Asia Standard Time" to mapOf(
        "AQ" to "Antarctica/Davis",
        "CX" to "Indian/Christmas",
        "ID" to "Asia/Jakarta",
        "ID2" to "Asia/Pontianak",
        "KH" to "Asia/Phnom_Penh",
        "LA" to "Asia/Vientiane",
        "TH" to "Asia/Bangkok",
        "VN" to "Asia/Saigon",
        "ZZ" to "Etc/GMT-7"
    ).withDefault {
        "Asia/Bangkok"
    },
    "Altai Standard Time" to mapOf(
        "RU" to "Asia/Barnaul"
    ).withDefault {
        "Asia/Barnaul"
    },
    "W. Mongolia Standard Time" to mapOf(
        "MN" to "Asia/Hovd"
    ).withDefault {
        "Asia/Hovd"
    },
    "North Asia Standard Time" to mapOf(
        "RU" to "Asia/Krasnoyarsk",
        "RU2" to "Asia/Novokuznetsk"
    ).withDefault {
        "Asia/Krasnoyarsk"
    },
    "N. Central Asia Standard Time" to mapOf(
        "RU" to "Asia/Novosibirsk"
    ).withDefault {
        "Asia/Novosibirsk"
    },
    "Tomsk Standard Time" to mapOf(
        "RU" to "Asia/Tomsk"
    ).withDefault {
        "Asia/Tomsk"
    },
    "China Standard Time" to mapOf(
        "CN" to "Asia/Shanghai",
        "HK" to "Asia/Hong_Kong",
        "MO" to "Asia/Macau"
    ).withDefault {
        "Asia/Shanghai"
    },
    "North Asia East Standard Time" to mapOf(
        "RU" to "Asia/Irkutsk"
    ).withDefault {
        "Asia/Irkutsk"
    },
    "Singapore Standard Time" to mapOf(
        "BN" to "Asia/Brunei",
        "ID" to "Asia/Makassar",
        "MY" to "Asia/Kuala_Lumpur",
        "MY2" to "Asia/Kuching",
        "PH" to "Asia/Manila",
        "SG" to "Asia/Singapore",
        "ZZ" to "Etc/GMT-8"
    ).withDefault {
        "Asia/Singapore"
    },
    "W. Australia Standard Time" to mapOf(
        "AQ" to "Antarctica/Casey",
        "AU" to "Australia/Perth"
    ).withDefault {
        "Australia/Perth"
    },
    "Taipei Standard Time" to mapOf(
        "TW" to "Asia/Taipei"
    ).withDefault {
        "Asia/Taipei"
    },
    "Ulaanbaatar Standard Time" to mapOf(
        "MN" to "Asia/Ulaanbaatar",
        "MN2" to "Asia/Choibalsan"
    ).withDefault {
        "Asia/Ulaanbaatar"
    },
    "Aus Central W. Standard Time" to mapOf(
        "AU" to "Australia/Eucla"
    ).withDefault {
        "Australia/Eucla"
    },
    "Transbaikal Standard Time" to mapOf(
        "RU" to "Asia/Chita"
    ).withDefault {
        "Asia/Chita"
    },
    "Tokyo Standard Time" to mapOf(
        "ID" to "Asia/Jayapura",
        "JP" to "Asia/Tokyo",
        "PW" to "Pacific/Palau",
        "TL" to "Asia/Dili",
        "ZZ" to "Etc/GMT-9"
    ).withDefault {
        "Asia/Tokyo"
    },
    "North Korea Standard Time" to mapOf(
        "KP" to "Asia/Pyongyang"
    ).withDefault {
        "Asia/Pyongyang"
    },
    "Korea Standard Time" to mapOf(
        "KR" to "Asia/Seoul"
    ).withDefault {
        "Asia/Seoul"
    },
    "Yakutsk Standard Time" to mapOf(
        "RU" to "Asia/Yakutsk",
        "RU2" to "Asia/Khandyga"
    ).withDefault {
        "Asia/Yakutsk"
    },
    "Cen. Australia Standard Time" to mapOf(
        "AU" to "Australia/Adelaide",
        "AU2" to "Australia/Broken_Hill"
    ).withDefault {
        "Australia/Adelaide"
    },
    "AUS Central Standard Time" to mapOf(
        "AU" to "Australia/Darwin"
    ).withDefault {
        "Australia/Darwin"
    },
    "E. Australia Standard Time" to mapOf(
        "AU" to "Australia/Brisbane",
        "AU2" to "Australia/Lindeman"
    ).withDefault {
        "Australia/Brisbane"
    },
    "AUS Eastern Standard Time" to mapOf(
        "AU" to "Australia/Sydney",
        "AU2" to "Australia/Melbourne"
    ).withDefault {
        "Australia/Sydney"
    },
    "West Pacific Standard Time" to mapOf(
        "AQ" to "Antarctica/DumontDUrville",
        "FM" to "Pacific/Truk",
        "GU" to "Pacific/Guam",
        "MP" to "Pacific/Saipan",
        "PG" to "Pacific/Port_Moresby",
        "ZZ" to "Etc/GMT-10"
    ).withDefault {
        "Pacific/Port_Moresby"
    },
    "Tasmania Standard Time" to mapOf(
        "AU" to "Australia/Hobart",
        "AU2" to "Australia/Currie"
    ).withDefault {
        "Australia/Hobart"
    },
    "Vladivostok Standard Time" to mapOf(
        "RU" to "Asia/Vladivostok",
        "RU2" to "Asia/Ust-Nera"
    ).withDefault {
        "Asia/Vladivostok"
    },
    "Lord Howe Standard Time" to mapOf(
        "AU" to "Australia/Lord_Howe"
    ).withDefault {
        "Australia/Lord_Howe"
    },
    "Bougainville Standard Time" to mapOf(
        "PG" to "Pacific/Bougainville"
    ).withDefault {
        "Pacific/Bougainville"
    },
    "Russia Time Zone 10" to mapOf(
        "RU" to "Asia/Srednekolymsk"
    ).withDefault {
        "Asia/Srednekolymsk"
    },
    "Magadan Standard Time" to mapOf(
        "RU" to "Asia/Magadan"
    ).withDefault {
        "Asia/Magadan"
    },
    "Norfolk Standard Time" to mapOf(
        "NF" to "Pacific/Norfolk"
    ).withDefault {
        "Pacific/Norfolk"
    },
    "Sakhalin Standard Time" to mapOf(
        "RU" to "Asia/Sakhalin"
    ).withDefault {
        "Asia/Sakhalin"
    },
    "Central Pacific Standard Time" to mapOf(
        "AU" to "Antarctica/Macquarie",
        "FM" to "Pacific/Ponape",
        "FM2" to "Pacific/Kosrae",
        "NC" to "Pacific/Noumea",
        "SB" to "Pacific/Guadalcanal",
        "VU" to "Pacific/Efate",
        "ZZ" to "Etc/GMT-11"
    ).withDefault {
        "Pacific/Guadalcanal"
    },
    "Russia Time Zone 11" to mapOf(
        "RU" to "Asia/Kamchatka",
        "RU2" to "Asia/Anadyr"
    ).withDefault {
        "Asia/Kamchatka"
    },
    "New Zealand Standard Time" to mapOf(
        "AQ" to "Antarctica/McMurdo",
        "NZ" to "Pacific/Auckland"
    ).withDefault {
        "Pacific/Auckland"
    },
    "UTC+12" to mapOf(
        "KI" to "Pacific/Tarawa",
        "MH" to "Pacific/Majuro",
        "MH2" to "Pacific/Kwajalein",
        "NR" to "Pacific/Nauru",
        "TV" to "Pacific/Funafuti",
        "UM" to "Pacific/Wake",
        "WF" to "Pacific/Wallis",
        "ZZ" to "Etc/GMT-12"
    ).withDefault {
        "Etc/GMT-12"
    },
    "Fiji Standard Time" to mapOf(
        "FJ" to "Pacific/Fiji"
    ).withDefault {
        "Pacific/Fiji"
    },
    "Chatham Islands Standard Time" to mapOf(
        "NZ" to "Pacific/Chatham"
    ).withDefault {
        "Pacific/Chatham"
    },
    "UTC+13" to mapOf(
        "KI" to "Pacific/Enderbury",
        "TK" to "Pacific/Fakaofo",
        "ZZ" to "Etc/GMT-13"
    ).withDefault {
        "Etc/GMT-13"
    },
    "Tonga Standard Time" to mapOf(
        "TO" to "Pacific/Tongatapu"
    ).withDefault {
        "Pacific/Tongatapu"
    },
    "Samoa Standard Time" to mapOf(
        "WS" to "Pacific/Apia"
    ).withDefault {
        "Pacific/Apia"
    },
    "Line Islands Standard Time" to mapOf(
        "KI" to "Pacific/Kiritimati",
        "ZZ" to "Etc/GMT-14"
    ).withDefault {
        "Pacific/Kiritimati"
    }
)
