package com.landable.app.data.responses

import com.landable.app.ui.home.dataModels.*
import org.json.JSONArray
import org.json.JSONObject

object ParseResponse {

    fun parseDashboardResponse(response: String): DashBoardDataModel {
        val dashboardInfo = DashBoardDataModel()
        val jsonObject = JSONObject(response)

/*
        val categorymasterArray = ArrayList<CategoriesDataModel>()
        val categoryArray = jsonObject.getJSONArray("categorymaster")
        for (i in 0 until categoryArray.length()) {
            val categories = CategoriesDataModel()
            categories.id = categoryArray.getJSONObject(i).getInt("id")
            categories.codevalue = categoryArray.getJSONObject(i).getString("codevalue")
            categories.icon = categoryArray.getJSONObject(i).getString("icon")
            categories.parent = categoryArray.getJSONObject(i).getInt("parent")
            categorymasterArray.add(categories)
        }
        dashboardInfo.categorymaster = categorymasterArray*/

        /*    val residentialTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
            val commercialTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
            val agriculturalTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
            val otherTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()


            val residentialPropertyArrayList = ArrayList<PropertyTypeDataModel>()
            val commercialPropertyArrayList = ArrayList<PropertyTypeDataModel>()
            val agriculturalPropertyArrayList = ArrayList<PropertyTypeDataModel>()
            val otherPropertyArrayList = ArrayList<PropertyTypeDataModel>()


            val propertyTypeArrayList = ArrayList<PropertyTypeDataModel>()
            val typeArray = jsonObject.getJSONArray("propertytypemaster")
            for (i in 0 until typeArray.length()) {
                val types = PropertyTypeDataModel()
                types.id = typeArray.getJSONObject(i).getInt("id")
                types.codevalue = typeArray.getJSONObject(i).getString("codevalue")
                types.icon = typeArray.getJSONObject(i).getString("icon")
                types.codetype = typeArray.getJSONObject(i).getString("codetype")
                types.parent = typeArray.getJSONObject(i).getInt("parent")

                if (typeArray.getJSONObject(i).getString("parent") == "1") {
                    residentialPropertyArrayList.add(types)
                } else if (typeArray.getJSONObject(i).getString("parent") == "2") {
                    commercialPropertyArrayList.add(types)
                } else if (typeArray.getJSONObject(i).getString("parent") == "3") {
                    agriculturalPropertyArrayList.add(types)
                } else {
                    otherPropertyArrayList.add(types)
                }

                residentialTypeLinkedList["Residential"] = residentialPropertyArrayList
                commercialTypeLinkedList["Commercial"] = commercialPropertyArrayList
                agriculturalTypeLinkedList["Agricultural"] = agriculturalPropertyArrayList

                propertyTypeArrayList.add(types)

                dashboardInfo.residentialTypeLinkedHashMap = residentialTypeLinkedList
                dashboardInfo.commercialTypeLinkedList = commercialTypeLinkedList
                dashboardInfo.agriculturalTypeLinkedList = agriculturalTypeLinkedList
                dashboardInfo.otherTypeLinkedList = otherTypeLinkedList
            }
            dashboardInfo.propertytypemaster = propertyTypeArrayList

    */
        val whyLandableArrayList = ArrayList<WhyLandableDataModel>()
        val whyArray = jsonObject.getJSONArray("whylandable")
        for (i in 0 until whyArray.length()) {
            val whyLandable = WhyLandableDataModel()
            whyLandable.id = whyArray.getJSONObject(i).getInt("id")
            whyLandable.pages = whyArray.getJSONObject(i).getString("pages")
            whyLandable.shortdescription = whyArray.getJSONObject(i).getString("shortdescription")
            whyLandable.appicon = whyArray.getJSONObject(i).getString("appicon")
            whyLandableArrayList.add(whyLandable)
        }
        dashboardInfo.whylandable = whyLandableArrayList


        val featurePropertiesArrayList = ArrayList<FeaturePropertiesDataModel>()
        val propertiesArray = jsonObject.getJSONArray("featuredproperties")
        for (i in 0 until propertiesArray.length()) {
            val featureProperties = FeaturePropertiesDataModel()
            featureProperties.id = propertiesArray.getJSONObject(i).getInt("id")
            featureProperties.saletypename =
                propertiesArray.getJSONObject(i).getString("saletypename")
            featureProperties.propertyid = propertiesArray.getJSONObject(i).getString("propertyid")
            featureProperties.projectid = propertiesArray.getJSONObject(i).getString("projectid")
            featureProperties.title = propertiesArray.getJSONObject(i).getString("title")
            featureProperties.description =
                propertiesArray.getJSONObject(i).getString("description")
            featureProperties.bedroom = propertiesArray.getJSONObject(i).getString("bedroom")
            featureProperties.bathroom = propertiesArray.getJSONObject(i).getString("bathroom")
            featureProperties.totalarea = propertiesArray.getJSONObject(i).getString("totalarea")
            featureProperties.categoryname =
                propertiesArray.getJSONObject(i).getString("categoryname")
            featureProperties.subcategoryname =
                propertiesArray.getJSONObject(i).getString("subcategoryname")
            featureProperties.possessionname =
                propertiesArray.getJSONObject(i).getString("possessionname")
            featureProperties.cityname = propertiesArray.getJSONObject(i).getString("cityname")
            featureProperties.costinword = propertiesArray.getJSONObject(i).getString("costinword")
            featureProperties.image1 = propertiesArray.getJSONObject(i).getString("image1")
            featureProperties.isverified = propertiesArray.getJSONObject(i).getString("isverified")
            featureProperties.verfiediconpath =
                propertiesArray.getJSONObject(i).getString("verfiediconpath")
            featureProperties.link = propertiesArray.getJSONObject(i).getString("link")
            featureProperties.customertype =
                propertiesArray.getJSONObject(i).getString("customertype")
            featureProperties.addedbyid = propertiesArray.getJSONObject(i).getInt("addedbyid")
            featureProperties.name = propertiesArray.getJSONObject(i).getString("name")
            featureProperties.profilepic = propertiesArray.getJSONObject(i).getString("profilepic")
            featureProperties.badges = propertiesArray.getJSONObject(i).getString("badges")
            featureProperties.strength = propertiesArray.getJSONObject(i).getDouble("strength")
            featureProperties.strengthmsg =
                propertiesArray.getJSONObject(i).getString("strengthmsg")
            featureProperties.address = propertiesArray.getJSONObject(i).getString("address")
            featureProperties.rating = propertiesArray.getJSONObject(i).getDouble("rating")
            featureProperties.lat = propertiesArray.getJSONObject(i).getString("lat")
            featureProperties.lon = propertiesArray.getJSONObject(i).getString("lon")
            featureProperties.isfavourite =
                propertiesArray.getJSONObject(i).getString("isfavourite")
            featurePropertiesArrayList.add(featureProperties)
        }
        dashboardInfo.featuredproperties = featurePropertiesArrayList


        val recentPropertiesArrayList = ArrayList<FeaturePropertiesDataModel>()
        val recentArray = jsonObject.getJSONArray("recentproperties")
        for (i in 0 until recentArray.length()) {
            val recentProperties = FeaturePropertiesDataModel()
            recentProperties.id = recentArray.getJSONObject(i).getInt("id")
            recentProperties.saletypename =
                recentArray.getJSONObject(i).getString("saletypename")
            recentProperties.propertyid = recentArray.getJSONObject(i).getString("propertyid")
            recentProperties.projectid = recentArray.getJSONObject(i).getString("projectid")
            recentProperties.title = recentArray.getJSONObject(i).getString("title")
            recentProperties.description =
                recentArray.getJSONObject(i).getString("description")
            recentProperties.bedroom = recentArray.getJSONObject(i).getString("bedroom")
            recentProperties.bathroom = recentArray.getJSONObject(i).getString("bathroom")
            recentProperties.totalarea = recentArray.getJSONObject(i).getString("totalarea")
            recentProperties.categoryname =
                recentArray.getJSONObject(i).getString("categoryname")
            recentProperties.subcategoryname =
                recentArray.getJSONObject(i).getString("subcategoryname")
            recentProperties.possessionname =
                recentArray.getJSONObject(i).getString("possessionname")
            recentProperties.cityname = recentArray.getJSONObject(i).getString("cityname")
            recentProperties.costinword = recentArray.getJSONObject(i).getString("costinword")
            recentProperties.image1 = recentArray.getJSONObject(i).getString("image1")
            recentProperties.isverified = recentArray.getJSONObject(i).getString("isverified")
            recentProperties.verfiediconpath =
                recentArray.getJSONObject(i).getString("verfiediconpath")
            recentProperties.link = recentArray.getJSONObject(i).getString("link")
            recentProperties.customertype =
                recentArray.getJSONObject(i).getString("customertype")
            recentProperties.addedbyid = recentArray.getJSONObject(i).getInt("addedbyid")
            recentProperties.name = recentArray.getJSONObject(i).getString("name")
            recentProperties.profilepic = recentArray.getJSONObject(i).getString("profilepic")
            recentProperties.badges = recentArray.getJSONObject(i).getString("badges")
            recentProperties.strength = recentArray.getJSONObject(i).getDouble("strength")
            recentProperties.strengthmsg = recentArray.getJSONObject(i).getString("strengthmsg")
            recentProperties.address = recentArray.getJSONObject(i).getString("address")
            recentProperties.rating = recentArray.getJSONObject(i).getDouble("rating")
            recentProperties.lat = recentArray.getJSONObject(i).getString("lat")
            recentProperties.lon = recentArray.getJSONObject(i).getString("lon")
            recentProperties.isfavourite = propertiesArray.getJSONObject(i).getString("isfavourite")

            recentPropertiesArrayList.add(recentProperties)
        }
        dashboardInfo.recentproperties = recentPropertiesArrayList


        val projectsArrayList = ArrayList<ProjectsDataModel>()
        val projectsArray = jsonObject.getJSONArray("projects")
        for (i in 0 until projectsArray.length()) {
            val projects = ProjectsDataModel()
            projects.id = projectsArray.getJSONObject(i).getInt("id")
            projects.projectname =
                projectsArray.getJSONObject(i).getString("projectname")
            projects.Fulladdress = projectsArray.getJSONObject(i).getString("Fulladdress")
            projects.projectid = projectsArray.getJSONObject(i).getString("projectid")
            projects.mincost = projectsArray.getJSONObject(i).getString("mincost")
            projects.description =
                projectsArray.getJSONObject(i).getString("ddescription")
            projects.maxcost = projectsArray.getJSONObject(i).getString("maxcost")
            projects.configuration = projectsArray.getJSONObject(i).getString("configuration")
            projects.configuration2 = projectsArray.getJSONObject(i).getString("configuration2")
            projects.categoryname =
                projectsArray.getJSONObject(i).getString("categoryname")
            projects.subcategoryname =
                projectsArray.getJSONObject(i).getString("subcategoryname")
            projects.possessionname =
                projectsArray.getJSONObject(i).getString("possessionname")
            projects.image1 = projectsArray.getJSONObject(i).getString("image1")
            projects.customertype =
                projectsArray.getJSONObject(i).getString("customertype")
            projects.addedbyid = projectsArray.getJSONObject(i).getInt("addedbyid")
            projects.name = projectsArray.getJSONObject(i).getString("name")
            projects.strength = projectsArray.getJSONObject(i).getDouble("strength")
            projects.strengthmsg = projectsArray.getJSONObject(i).getString("strengthmsg")
            projects.rating = projectsArray.getJSONObject(i).getDouble("rating")
            projects.lat = projectsArray.getJSONObject(i).getString("lat")
            projects.lon = projectsArray.getJSONObject(i).getString("lon")
            projects.isfavourite = projectsArray.getJSONObject(i).getString("isfavourite")
            projects.linkurl = projectsArray.getJSONObject(i).getString("linkurl")
            projects.badges = projectsArray.getJSONObject(i).getString("badges")
            projects.profilepic = projectsArray.getJSONObject(i).getString("profilepic")

            projectsArrayList.add(projects)
        }
        dashboardInfo.projects = projectsArrayList
        return dashboardInfo
    }

    fun parseStateCityResponse(response: String): FilterMasterDataModel {
        val filterInfo = FilterMasterDataModel()
        val jsonObject = JSONObject(response)


        val stateArray = ArrayList<Statemaster>()
        val state = jsonObject.getJSONArray("statemaster")
        for (i in 0 until state.length()) {
            val stateMaster = Statemaster()
            stateMaster.id = state.getJSONObject(i).getInt("id")
            stateMaster.state = state.getJSONObject(i).getString("state")
            stateArray.add(stateMaster)
        }
        filterInfo.statemaster = stateArray


        val cityLinkedHasMap = LinkedHashMap<Int, ArrayList<Citymaster>>()
        val cityArray = jsonObject.getJSONArray("citymaster")
        for (i in 0 until cityArray.length()) {
            val cities = Citymaster()
            cities.id = cityArray.getJSONObject(i).getInt("id")
            cities.state = cityArray.getJSONObject(i).getInt("state")
            cities.city = cityArray.getJSONObject(i).getString("city")

            if (cityLinkedHasMap.containsKey(cities.state)) {
                val cityArrayList = cityLinkedHasMap[cities.state]
                cityArrayList!!.add(cities)
            } else {
                val citymasterArray = ArrayList<Citymaster>()
                citymasterArray.add(cities)

                cityLinkedHasMap[cities.state] = citymasterArray
            }
        }

        filterInfo.cityHashMap = cityLinkedHasMap


        val unitmasterArray = ArrayList<Unitmaster>()
        val unitArray = jsonObject.getJSONArray("Unitmaster")
        for (i in 0 until unitArray.length()) {
            val unit = Unitmaster()
            unit.id = unitArray.getJSONObject(i).getInt("id")
            unit.codevalue = unitArray.getJSONObject(i).getString("codevalue")
            unit.abbr = unitArray.getJSONObject(i).getString("abbr")
            unit.codetype = unitArray.getJSONObject(i).getString("codetype")
            unit.parent = unitArray.getJSONObject(i).getInt("parent")
            unitmasterArray.add(unit)
        }
        filterInfo.Unitmaster = unitmasterArray

        val amenitiesmasterArray = ArrayList<Amenitiesmaster>()
        val amenitiesArray = jsonObject.getJSONArray("Amenitiesmaster")
        for (i in 0 until amenitiesArray.length()) {
            val amenities = Amenitiesmaster()
            amenities.id = amenitiesArray.getJSONObject(i).getInt("id")
            amenities.codevalue = amenitiesArray.getJSONObject(i).getString("codevalue")
            amenities.icon = amenitiesArray.getJSONObject(i).getString("icon")
            amenities.codetype = amenitiesArray.getJSONObject(i).getString("codetype")
            amenities.parent = amenitiesArray.getJSONObject(i).getInt("parent")
            amenitiesmasterArray.add(amenities)
        }
        filterInfo.Amenitiesmaster = amenitiesmasterArray


        return filterInfo
    }

    fun parsePostPropertyBasicInfoFilterResponse(response: String): FilterMasterDataModel {
        val filterInfo = FilterMasterDataModel()
        val jsonObject = JSONObject(response)

        val unitmasterArray = ArrayList<Unitmaster>()
        val unitArray = jsonObject.getJSONArray("Unitmaster")
        for (i in 0 until unitArray.length()) {
            val unit = Unitmaster()
            unit.id = unitArray.getJSONObject(i).getInt("id")
            unit.codevalue = unitArray.getJSONObject(i).getString("codevalue")
            unit.abbr = unitArray.getJSONObject(i).getString("abbr")
            unit.codetype = unitArray.getJSONObject(i).getString("codetype")
            unit.parent = unitArray.getJSONObject(i).getInt("parent")
            unitmasterArray.add(unit)
        }
        filterInfo.Unitmaster = unitmasterArray

        val categorymasterArray = ArrayList<CategoriesDataModel>()
        val categoryArray = jsonObject.getJSONArray("categorymaster")
        for (i in 0 until categoryArray.length()) {
            val categories = CategoriesDataModel()
            categories.id = categoryArray.getJSONObject(i).getInt("id")
            categories.codevalue = categoryArray.getJSONObject(i).getString("codevalue")
            categories.icon = categoryArray.getJSONObject(i).getString("icon")
            categories.parent = categoryArray.getJSONObject(i).getInt("parent")
            categorymasterArray.add(categories)
        }
        filterInfo.categorymaster = categorymasterArray


        val arbitragemasterArray = ArrayList<Arbitragemaster>()
        val arbitrageArray = jsonObject.getJSONArray("Arbitragemaster")
        for (i in 0 until arbitrageArray.length()) {
            val arbitrage = Arbitragemaster()
            arbitrage.id = arbitrageArray.getJSONObject(i).getInt("id")
            arbitrage.codevalue = arbitrageArray.getJSONObject(i).getString("codevalue")
            arbitrage.icon = arbitrageArray.getJSONObject(i).getString("icon")
            arbitrage.codetype = arbitrageArray.getJSONObject(i).getString("codetype")
            arbitrage.parent = arbitrageArray.getJSONObject(i).getInt("parent")
            arbitragemasterArray.add(arbitrage)
        }
        filterInfo.Arbitragemaster = arbitragemasterArray

        val floormasterArray = ArrayList<Floormaster>()
        val floorArray = jsonObject.getJSONArray("floormaster")
        for (i in 0 until floorArray.length()) {
            val floors = Floormaster()
            floors.floor = floorArray.getJSONObject(i).getString("floor")
            floors.floorvalue = floorArray.getJSONObject(i).getString("floorvalue")
            floormasterArray.add(floors)
        }
        filterInfo.floormaster = floormasterArray

        val bedroomArray = ArrayList<Bedroom>()
        val bedroom = jsonObject.getJSONArray("bedroom")
        for (i in 0 until bedroom.length()) {
            val bedroomMaster = Bedroom()
            bedroomMaster.id = bedroom.getJSONObject(i).getInt("id")
            bedroomMaster.value = bedroom.getJSONObject(i).getString("value")
            bedroomArray.add(bedroomMaster)
        }
        filterInfo.bedroom = bedroomArray

        val possessionmasterArray = ArrayList<Arbitragemaster>()
        val possessionArray = jsonObject.getJSONArray("possessionmaster")
        for (i in 0 until possessionArray.length()) {
            val possession = Arbitragemaster()
            possession.id = possessionArray.getJSONObject(i).getInt("id")
            possession.codevalue = possessionArray.getJSONObject(i).getString("codevalue")
            possession.codetype = possessionArray.getJSONObject(i).getString("codetype")
            possession.icon = possessionArray.getJSONObject(i).getString("icon")
            possession.parent = possessionArray.getJSONObject(i).getInt("parent")
            possessionmasterArray.add(possession)
        }
        filterInfo.possessionmaster = possessionmasterArray


        val residentialTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
        val commercialTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
        val agriculturalTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
        val otherTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()


        val residentialPropertyArrayList = ArrayList<PropertyTypeDataModel>()
        val commercialPropertyArrayList = ArrayList<PropertyTypeDataModel>()
        val agriculturalPropertyArrayList = ArrayList<PropertyTypeDataModel>()
        val otherPropertyArrayList = ArrayList<PropertyTypeDataModel>()


        val propertyTypeArrayList = ArrayList<PropertyTypeDataModel>()
        val typeArray = jsonObject.getJSONArray("propertytypemaster")
        for (i in 0 until typeArray.length()) {
            val types = PropertyTypeDataModel()
            types.id = typeArray.getJSONObject(i).getInt("id")
            types.codevalue = typeArray.getJSONObject(i).getString("codevalue")
            types.icon = typeArray.getJSONObject(i).getString("icon")
            types.codetype = typeArray.getJSONObject(i).getString("codetype")
            types.parent = typeArray.getJSONObject(i).getInt("parent")

            when {
                typeArray.getJSONObject(i).getString("parent") == "1" -> {
                    residentialPropertyArrayList.add(types)
                }
                typeArray.getJSONObject(i).getString("parent") == "2" -> {
                    commercialPropertyArrayList.add(types)
                }
                typeArray.getJSONObject(i).getString("parent") == "3" -> {
                    agriculturalPropertyArrayList.add(types)
                }
                else -> {
                    otherPropertyArrayList.add(types)
                }
            }

            residentialTypeLinkedList["Residential"] = residentialPropertyArrayList
            commercialTypeLinkedList["Commercial"] = commercialPropertyArrayList
            agriculturalTypeLinkedList["Agricultural"] = agriculturalPropertyArrayList

            propertyTypeArrayList.add(types)

            filterInfo.residentialTypeLinkedHashMap = residentialTypeLinkedList
            filterInfo.commercialTypeLinkedList = commercialTypeLinkedList
            filterInfo.agriculturalTypeLinkedList = agriculturalTypeLinkedList
            filterInfo.otherTypeLinkedList = otherTypeLinkedList
        }
        filterInfo.propertytypemaster = propertyTypeArrayList

        val saletype = ArrayList<Arbitragemaster>()
        val sale = jsonObject.getJSONArray("saletypemaster")
        for (i in 0 until sale.length()) {
            val saleMaster = Arbitragemaster()

            saleMaster.id = sale.getJSONObject(i).getInt("id")
            saleMaster.codevalue = sale.getJSONObject(i).getString("codevalue")
            saleMaster.codetype = sale.getJSONObject(i).getString("codetype")
            saleMaster.icon = sale.getJSONObject(i).getString("icon")
            saleMaster.parent = sale.getJSONObject(i).getInt("parent")
            saletype.add(saleMaster)
        }
        filterInfo.saletypemaster = saletype

        return filterInfo
    }

    fun parseAuctionFilterMasterResponse(response: String): FilterMasterDataModel {
        val filterInfo = FilterMasterDataModel()
        val jsonObject = JSONObject(response)


        val stateArray = ArrayList<Statemaster>()
        val state = jsonObject.getJSONArray("statemaster")
        for (i in 0 until state.length()) {
            val stateMaster = Statemaster()
            stateMaster.id = state.getJSONObject(i).getInt("id")
            stateMaster.state = state.getJSONObject(i).getString("state")
            stateArray.add(stateMaster)
        }
        filterInfo.statemaster = stateArray


        val cityLinkedHasMap = LinkedHashMap<Int, ArrayList<Citymaster>>()
        val cityArray = jsonObject.getJSONArray("citymaster")
        for (i in 0 until cityArray.length()) {
            val cities = Citymaster()
            cities.id = cityArray.getJSONObject(i).getInt("id")
            cities.state = cityArray.getJSONObject(i).getInt("state")
            cities.city = cityArray.getJSONObject(i).getString("city")

            if (cityLinkedHasMap.containsKey(cities.state)) {
                val cityArrayList = cityLinkedHasMap[cities.state]
                cityArrayList!!.add(cities)
            } else {
                val citymasterArray = ArrayList<Citymaster>()
                citymasterArray.add(cities)

                cityLinkedHasMap[cities.state] = citymasterArray
            }
        }

        filterInfo.cityHashMap = cityLinkedHasMap


        val borrowerArray = ArrayList<Borrowernamemaster>()
        val borrower = jsonObject.getJSONArray("borrowernamemaster")
        for (i in 0 until borrower.length()) {
            val borrowerMaster = Borrowernamemaster()
            borrowerMaster.borrowername = borrower.getJSONObject(i).getString("borrowername")
            borrowerArray.add(borrowerMaster)
        }
        filterInfo.borrowernamemaster = borrowerArray

        val categorymasterArray = ArrayList<CategoriesDataModel>()
        val categoryArray = jsonObject.getJSONArray("categorymaster")
        for (i in 0 until categoryArray.length()) {
            val categories = CategoriesDataModel()
            categories.id = categoryArray.getJSONObject(i).getInt("id")
            categories.codevalue = categoryArray.getJSONObject(i).getString("codevalue")
            categories.icon = categoryArray.getJSONObject(i).getString("icon")
            categories.parent = categoryArray.getJSONObject(i).getInt("parent")
            categorymasterArray.add(categories)
        }
        filterInfo.categorymaster = categorymasterArray

        val residentialTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
        val commercialTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
        val agriculturalTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
        val otherTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()


        val residentialPropertyArrayList = ArrayList<PropertyTypeDataModel>()
        val commercialPropertyArrayList = ArrayList<PropertyTypeDataModel>()
        val agriculturalPropertyArrayList = ArrayList<PropertyTypeDataModel>()
        val otherPropertyArrayList = ArrayList<PropertyTypeDataModel>()


        val propertyTypeArrayList = ArrayList<PropertyTypeDataModel>()
        val typeArray = jsonObject.getJSONArray("propertytypemaster")
        for (i in 0 until typeArray.length()) {
            val types = PropertyTypeDataModel()
            types.id = typeArray.getJSONObject(i).getInt("id")
            types.codevalue = typeArray.getJSONObject(i).getString("codevalue")
            types.icon = typeArray.getJSONObject(i).getString("icon")
            types.codetype = typeArray.getJSONObject(i).getString("codetype")
            types.parent = typeArray.getJSONObject(i).getInt("parent")

            when {
                typeArray.getJSONObject(i).getString("parent") == "1" -> {
                    residentialPropertyArrayList.add(types)
                }
                typeArray.getJSONObject(i).getString("parent") == "2" -> {
                    commercialPropertyArrayList.add(types)
                }
                typeArray.getJSONObject(i).getString("parent") == "3" -> {
                    agriculturalPropertyArrayList.add(types)
                }
                else -> {
                    otherPropertyArrayList.add(types)
                }
            }

            residentialTypeLinkedList["Residential"] = residentialPropertyArrayList
            commercialTypeLinkedList["Commercial"] = commercialPropertyArrayList
            agriculturalTypeLinkedList["Agricultural"] = agriculturalPropertyArrayList

            propertyTypeArrayList.add(types)

            filterInfo.residentialTypeLinkedHashMap = residentialTypeLinkedList
            filterInfo.commercialTypeLinkedList = commercialTypeLinkedList
            filterInfo.agriculturalTypeLinkedList = agriculturalTypeLinkedList
            filterInfo.otherTypeLinkedList = otherTypeLinkedList
        }
        filterInfo.propertytypemaster = propertyTypeArrayList


        val unitmasterArray = ArrayList<Unitmaster>()
        val unitArray = jsonObject.getJSONArray("Unitmaster")
        for (i in 0 until unitArray.length()) {
            val unit = Unitmaster()
            unit.id = unitArray.getJSONObject(i).getInt("id")
            unit.codevalue = unitArray.getJSONObject(i).getString("codevalue")
            unit.abbr = unitArray.getJSONObject(i).getString("abbr")
            unit.codetype = unitArray.getJSONObject(i).getString("codetype")
            unit.parent = unitArray.getJSONObject(i).getInt("parent")
            unitmasterArray.add(unit)
        }
        filterInfo.Unitmaster = unitmasterArray

        val bankArray = ArrayList<Banknamemaster>()
        val bank = jsonObject.getJSONArray("banknamemaster")
        for (i in 0 until bank.length()) {
            val bankMaster = Banknamemaster()
            bankMaster.bankname = bank.getJSONObject(i).getString("bankname")
            bankArray.add(bankMaster)
        }
        filterInfo.banknamemaster = bankArray


        return filterInfo
    }

    fun parsePropertyRawDataModel(response: String): PropertyRawDataModel {
        val propertyRaw = PropertyRawDataModel()
        val jsonObject = JSONObject(response)


        val amenitiesmasterArray = ArrayList<Amenitiesmaster>()
        val amenitiesArray = jsonObject.getJSONArray("Amenitiesmasters")
        for (i in 0 until amenitiesArray.length()) {
            val amenities = Amenitiesmaster()
            amenities.id = amenitiesArray.getJSONObject(i).getInt("id")
            amenities.codevalue = amenitiesArray.getJSONObject(i).getString("codevalue")
            amenities.icon = amenitiesArray.getJSONObject(i).getString("icon")
            amenities.codetype = amenitiesArray.getJSONObject(i).getString("codetype")
            amenities.parent = amenitiesArray.getJSONObject(i).getInt("parent")
            amenitiesmasterArray.add(amenities)
        }
        propertyRaw.Amenitiesmasters = amenitiesmasterArray


        val propertyimagesmasterArray = ArrayList<Propertyimage>()
        val propertyimagesArray = jsonObject.getJSONArray("propertyimages")
        for (i in 0 until propertyimagesArray.length()) {
            val propertyimages = Propertyimage()


            propertyimages.id = propertyimagesArray.getJSONObject(i).getInt("id")
            propertyimages.propertyid = propertyimagesArray.getJSONObject(i).getInt("propertyid")
            propertyimages.imagepath = propertyimagesArray.getJSONObject(i).getString("imagepath")
            propertyimages.imagetype = propertyimagesArray.getJSONObject(i).getString("imagetype")

            propertyimagesmasterArray.add(propertyimages)
        }
        propertyRaw.propertyimages = propertyimagesmasterArray


        val propertyRawmasterArray = ArrayList<Propertyraw>()
        val propertyRawArray = jsonObject.getJSONArray("propertyraw")
        for (i in 0 until propertyRawArray.length()) {
            val propertyraw = Propertyraw()


            propertyraw.addedbyid = propertyRawArray.getJSONObject(i).getInt("addedbyid")
            propertyraw.attachedbathroom =
                propertyRawArray.getJSONObject(i).getInt("attachedbathroom")
            propertyraw.anyconstruction =
                propertyRawArray.getJSONObject(i).getString("anyconstruction")
            propertyraw.address1 = propertyRawArray.getJSONObject(i).getString("address1")
            propertyraw.Landmark = propertyRawArray.getJSONObject(i).getString("Landmark")
            propertyraw.Additionalroom =
                propertyRawArray.getJSONObject(i).getString("Additionalroom")
            propertyraw.availfrommonth =
                propertyRawArray.getJSONObject(i).getInt("availfrommonth")
            propertyraw.availfromyear = propertyRawArray.getJSONObject(i).getInt("availfromyear")
            propertyraw.balcony = propertyRawArray.getJSONObject(i).getInt("balcony")
            propertyraw.bathroom = propertyRawArray.getJSONObject(i).getInt("bathroom")
            propertyraw.bedroom = propertyRawArray.getJSONObject(i).getInt("bedroom")
            propertyraw.boundrywall = propertyRawArray.getJSONObject(i).getString("boundrywall")
            propertyraw.city = propertyRawArray.getJSONObject(i).getInt("city")
            propertyraw.category = propertyRawArray.getJSONObject(i).getInt("category")
            propertyraw.carpetarea = propertyRawArray.getJSONObject(i).getDouble("carpetarea")
            propertyraw.bulityear = propertyRawArray.getJSONObject(i).getInt("bulityear")
            propertyraw.builduparea = propertyRawArray.getJSONObject(i).getDouble("builduparea")
            propertyraw.costinword = propertyRawArray.getJSONObject(i).getString("costinword")
            propertyraw.costperft = propertyRawArray.getJSONObject(i).getString("costperft")
            propertyraw.country = propertyRawArray.getJSONObject(i).getInt("country")
            propertyraw.createddate = propertyRawArray.getJSONObject(i).getString("createddate")
            propertyraw.cost = propertyRawArray.getJSONObject(i).getDouble("cost")
            propertyraw.depositepercentage =
                propertyRawArray.getJSONObject(i).getDouble("depositepercentage")
            propertyraw.featured = propertyRawArray.getJSONObject(i).getInt("featured")
            propertyraw.furnished = propertyRawArray.getJSONObject(i).getInt("furnished")
            propertyraw.floorno = propertyRawArray.getJSONObject(i).getString("floorno")
            propertyraw.floorforconstruction =
                propertyRawArray.getJSONObject(i).getInt("floorforconstruction")
            propertyraw.expirydate = propertyRawArray.getJSONObject(i).getString("expirydate")
            propertyraw.description = propertyRawArray.getJSONObject(i).getString("description")
            propertyraw.isingatedcolony =
                propertyRawArray.getJSONObject(i).getString("isingatedcolony")
            propertyraw.iscorner = propertyRawArray.getJSONObject(i).getString("iscorner")
            propertyraw.id = propertyRawArray.getJSONObject(i).getInt("id")
            propertyraw.lastremodalyear =
                propertyRawArray.getJSONObject(i).getString("lastremodalyear")
            propertyraw.lat = propertyRawArray.getJSONObject(i).getString("lat")
            propertyraw.link = propertyRawArray.getJSONObject(i).getString("link")
            propertyraw.lon = propertyRawArray.getJSONObject(i).getString("lon")
            propertyraw.maintenancecharge =
                propertyRawArray.getJSONObject(i).getDouble("maintenancecharge")
            propertyraw.maplocation = propertyRawArray.getJSONObject(i).getString("maplocation")
            propertyraw.openside = propertyRawArray.getJSONObject(i).getInt("openside")
            propertyraw.parking = propertyRawArray.getJSONObject(i).getInt("parking")
            propertyraw.pincode = propertyRawArray.getJSONObject(i).getString("pincode")
            propertyraw.poolsize = propertyRawArray.getJSONObject(i).getString("poolsize")
            propertyraw.propertyid = propertyRawArray.getJSONObject(i).getString("propertyid")
            propertyraw.possessionstatus =
                propertyRawArray.getJSONObject(i).getInt("possessionstatus")
            propertyraw.projectid = propertyRawArray.getJSONObject(i).getInt("projectid")
            propertyraw.rating = propertyRawArray.getJSONObject(i).getDouble("rating")
            propertyraw.roadfacing = propertyRawArray.getJSONObject(i).getString("roadfacing")
            propertyraw.saletype = propertyRawArray.getJSONObject(i).getInt("saletype")
            propertyraw.subcategory = propertyRawArray.getJSONObject(i).getInt("subcategory")
            propertyraw.state = propertyRawArray.getJSONObject(i).getInt("state")
            propertyraw.securitydeposite =
                propertyRawArray.getJSONObject(i).getDouble("securitydeposite")
            propertyraw.totalarea = propertyRawArray.getJSONObject(i).getDouble("totalarea")
            propertyraw.unit = propertyRawArray.getJSONObject(i).getString("unit")
            propertyraw.status = propertyRawArray.getJSONObject(i).getString("status")
            propertyraw.title = propertyRawArray.getJSONObject(i).getString("title")

            propertyRawmasterArray.add(propertyraw)
        }
        propertyRaw.propertyraw = propertyRawmasterArray



        return propertyRaw
    }

    fun parseFilterMasterResponse(response: String): FilterMasterDataModel {
        val filterInfo = FilterMasterDataModel()
        val jsonObject = JSONObject(response)


        val amenitiesmasterArray = ArrayList<Amenitiesmaster>()
        val amenitiesArray = jsonObject.getJSONArray("Amenitiesmaster")
        for (i in 0 until amenitiesArray.length()) {
            val amenities = Amenitiesmaster()
            amenities.id = amenitiesArray.getJSONObject(i).getInt("id")
            amenities.codevalue = amenitiesArray.getJSONObject(i).getString("codevalue")
            amenities.icon = amenitiesArray.getJSONObject(i).getString("icon")
            amenities.codetype = amenitiesArray.getJSONObject(i).getString("codetype")
            amenities.parent = amenitiesArray.getJSONObject(i).getInt("parent")
            amenitiesmasterArray.add(amenities)
        }
        filterInfo.Amenitiesmaster = amenitiesmasterArray


        val arbitragemasterArray = ArrayList<Arbitragemaster>()
        val arbitrageArray = jsonObject.getJSONArray("Arbitragemaster")
        for (i in 0 until arbitrageArray.length()) {
            val arbitrage = Arbitragemaster()
            arbitrage.id = arbitrageArray.getJSONObject(i).getInt("id")
            arbitrage.codevalue = arbitrageArray.getJSONObject(i).getString("codevalue")
            arbitrage.icon = arbitrageArray.getJSONObject(i).getString("icon")
            arbitrage.codetype = arbitrageArray.getJSONObject(i).getString("codetype")
            arbitrage.parent = arbitrageArray.getJSONObject(i).getInt("parent")
            arbitragemasterArray.add(arbitrage)
        }
        filterInfo.Arbitragemaster = arbitragemasterArray


        val auctionPossessiontypemasterArray = ArrayList<AuctionPossessiontypeMaster>()
        val auctionPossessiontypeArray = jsonObject.getJSONArray("AuctionPossessiontypeMaster")
        for (i in 0 until auctionPossessiontypeArray.length()) {
            val auctionPossessiontype = AuctionPossessiontypeMaster()
            auctionPossessiontype.id = auctionPossessiontypeArray.getJSONObject(i).getInt("id")
            auctionPossessiontype.codevalue =
                auctionPossessiontypeArray.getJSONObject(i).getString("codevalue")
            auctionPossessiontype.icon =
                auctionPossessiontypeArray.getJSONObject(i).getString("icon")
            auctionPossessiontype.codetype =
                auctionPossessiontypeArray.getJSONObject(i).getString("codetype")
            auctionPossessiontype.parent =
                auctionPossessiontypeArray.getJSONObject(i).getInt("parent")
            auctionPossessiontypemasterArray.add(auctionPossessiontype)
        }
        filterInfo.AuctionPossessiontypeMaster = auctionPossessiontypemasterArray


        val unitmasterArray = ArrayList<Unitmaster>()
        val unitArray = jsonObject.getJSONArray("Unitmaster")
        for (i in 0 until unitArray.length()) {
            val unit = Unitmaster()
            unit.id = unitArray.getJSONObject(i).getInt("id")
            unit.codevalue = unitArray.getJSONObject(i).getString("codevalue")
            unit.abbr = unitArray.getJSONObject(i).getString("abbr")
            unit.codetype = unitArray.getJSONObject(i).getString("codetype")
            unit.parent = unitArray.getJSONObject(i).getInt("parent")
            unitmasterArray.add(unit)
        }
        filterInfo.Unitmaster = unitmasterArray


        val balconeyArray = ArrayList<Bedroom>()
        val balconey = jsonObject.getJSONArray("balconey")
        for (i in 0 until balconey.length()) {
            val balconyMaster = Bedroom()
            balconyMaster.id = balconey.getJSONObject(i).getInt("id")
            balconyMaster.value = balconey.getJSONObject(i).getString("value")
            balconeyArray.add(balconyMaster)
        }
        filterInfo.balconey = balconeyArray


        val bankArray = ArrayList<Banknamemaster>()
        val bank = jsonObject.getJSONArray("banknamemaster")
        for (i in 0 until bank.length()) {
            val bankMaster = Banknamemaster()
            bankMaster.bankname = bank.getJSONObject(i).getString("bankname")
            bankArray.add(bankMaster)
        }
        filterInfo.banknamemaster = bankArray


        val bathroomArray = ArrayList<Bedroom>()
        val bathroom = jsonObject.getJSONArray("bathroom")
        for (i in 0 until bathroom.length()) {
            val bathroomMaster = Bedroom()
            bathroomMaster.id = bathroom.getJSONObject(i).getInt("id")
            bathroomMaster.value = bathroom.getJSONObject(i).getString("value")
            bathroomArray.add(bathroomMaster)
        }
        filterInfo.bathroom = bathroomArray


        val bedroomArray = ArrayList<Bedroom>()
        val bedroom = jsonObject.getJSONArray("bedroom")
        for (i in 0 until bedroom.length()) {
            val bedroomMaster = Bedroom()
            bedroomMaster.id = bedroom.getJSONObject(i).getInt("id")
            bedroomMaster.value = bedroom.getJSONObject(i).getString("value")
            bedroomArray.add(bedroomMaster)
        }
        filterInfo.bedroom = bedroomArray


        val borrowerArray = ArrayList<Borrowernamemaster>()
        val borrower = jsonObject.getJSONArray("borrowernamemaster")
        for (i in 0 until borrower.length()) {
            val borrowerMaster = Borrowernamemaster()
            borrowerMaster.borrowername = borrower.getJSONObject(i).getString("borrowername")
            borrowerArray.add(borrowerMaster)
        }
        filterInfo.borrowernamemaster = borrowerArray

        val categorymasterArray = ArrayList<CategoriesDataModel>()
        val categoryArray = jsonObject.getJSONArray("categorymaster")
        for (i in 0 until categoryArray.length()) {
            val categories = CategoriesDataModel()
            categories.id = categoryArray.getJSONObject(i).getInt("id")
            categories.codevalue = categoryArray.getJSONObject(i).getString("codevalue")
            categories.icon = categoryArray.getJSONObject(i).getString("icon")
            categories.parent = categoryArray.getJSONObject(i).getInt("parent")
            categorymasterArray.add(categories)
        }
        filterInfo.categorymaster = categorymasterArray

        /*      val cityLinkedHasMap = LinkedHashMap<Int, ArrayList<Citymaster>>()
              val cityArray = jsonObject.getJSONArray("citymaster")
              for (i in 0 until cityArray.length()) {
                  val cities = Citymaster()
                  cities.id = cityArray.getJSONObject(i).getInt("id")
                  cities.state = cityArray.getJSONObject(i).getInt("state")
                  cities.city = cityArray.getJSONObject(i).getString("city")

                  if (cityLinkedHasMap.containsKey(cities.state)) {
                      val cityArrayList = cityLinkedHasMap[cities.state]
                      cityArrayList!!.add(cities)
                  } else {
                      val citymasterArray = ArrayList<Citymaster>()
                      citymasterArray.add(cities)

                      cityLinkedHasMap[cities.state] = citymasterArray
                  }
              }
              filterInfo.cityHashMap = cityLinkedHasMap
      */

        val floormasterArray = ArrayList<Floormaster>()
        val floorArray = jsonObject.getJSONArray("floormaster")
        for (i in 0 until floorArray.length()) {
            val floors = Floormaster()
            floors.floor = floorArray.getJSONObject(i).getString("floor")
            floors.floorvalue = floorArray.getJSONObject(i).getString("floorvalue")
            floormasterArray.add(floors)
        }
        filterInfo.floormaster = floormasterArray


        val furnishedmasterArray = ArrayList<Arbitragemaster>()
        val furnishedArray = jsonObject.getJSONArray("furnishedmaster")
        for (i in 0 until furnishedArray.length()) {
            val furnished = Arbitragemaster()
            furnished.id = furnishedArray.getJSONObject(i).getInt("id")
            furnished.codevalue = furnishedArray.getJSONObject(i).getString("codevalue")
            furnished.icon = furnishedArray.getJSONObject(i).getString("icon")
            furnished.codetype = furnishedArray.getJSONObject(i).getString("codetype")
            furnished.parent = furnishedArray.getJSONObject(i).getInt("parent")
            furnishedmasterArray.add(furnished)
        }
        filterInfo.furnishedmaster = furnishedmasterArray


        val localitymasterArray = ArrayList<Localitymaster>()
        val localityArray = jsonObject.getJSONArray("localitymaster")
        for (i in 0 until localityArray.length()) {
            val locality = Localitymaster()
            locality.locality = localityArray.getJSONObject(i).getString("locality")
            localitymasterArray.add(locality)
        }
        filterInfo.localitymaster = localitymasterArray


        val mimaxauctionPriceRangeArray = ArrayList<MimaxauctionPriceRange>()
        val mimaxauctionPriceArray = jsonObject.getJSONArray("mimaxauctionPriceRange")
        for (i in 0 until mimaxauctionPriceArray.length()) {
            val minMax = MimaxauctionPriceRange()
            minMax.minprice = mimaxauctionPriceArray.getJSONObject(i).getDouble("minprice")
            minMax.maxprice = mimaxauctionPriceArray.getJSONObject(i).getDouble("maxprice")
            mimaxauctionPriceRangeArray.add(minMax)
        }
        filterInfo.mimaxauctionPriceRange = mimaxauctionPriceRangeArray


        val mimaxPropertyPriceRangeArray = ArrayList<MimaxpropertyPriceRange>()
        val mimaxPropertyPriceArray = jsonObject.getJSONArray("mimaxpropertyPriceRange")
        for (i in 0 until mimaxPropertyPriceArray.length()) {
            val minMax = MimaxpropertyPriceRange()
            minMax.mincost = mimaxPropertyPriceArray.getJSONObject(i).getInt("mincost")
            minMax.maxcost = mimaxPropertyPriceArray.getJSONObject(i).getInt("maxcost")
            mimaxPropertyPriceRangeArray.add(minMax)
        }
        filterInfo.mimaxpropertyPriceRange = mimaxPropertyPriceRangeArray


        val parkingArray = ArrayList<Bedroom>()
        val parking = jsonObject.getJSONArray("parking")
        for (i in 0 until parking.length()) {
            val parkingMaster = Bedroom()
            parkingMaster.id = parking.getJSONObject(i).getInt("id")
            parkingMaster.value = parking.getJSONObject(i).getString("value")
            parkingArray.add(parkingMaster)
        }
        filterInfo.parking = parkingArray


        val possessionmasterArray = ArrayList<Arbitragemaster>()
        val possessionArray = jsonObject.getJSONArray("possessionmaster")
        for (i in 0 until possessionArray.length()) {
            val possession = Arbitragemaster()
            possession.id = possessionArray.getJSONObject(i).getInt("id")
            possession.codevalue = possessionArray.getJSONObject(i).getString("codevalue")
            possession.codetype = possessionArray.getJSONObject(i).getString("codetype")
            possession.icon = possessionArray.getJSONObject(i).getString("icon")
            possession.parent = possessionArray.getJSONObject(i).getInt("parent")
            possessionmasterArray.add(possession)
        }
        filterInfo.possessionmaster = possessionmasterArray


        val residentialTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
        val commercialTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
        val agriculturalTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()
        val otherTypeLinkedList = LinkedHashMap<String, ArrayList<PropertyTypeDataModel>>()


        val residentialPropertyArrayList = ArrayList<PropertyTypeDataModel>()
        val commercialPropertyArrayList = ArrayList<PropertyTypeDataModel>()
        val agriculturalPropertyArrayList = ArrayList<PropertyTypeDataModel>()
        val otherPropertyArrayList = ArrayList<PropertyTypeDataModel>()


        val propertyTypeArrayList = ArrayList<PropertyTypeDataModel>()
        val typeArray = jsonObject.getJSONArray("propertytypemaster")
        for (i in 0 until typeArray.length()) {
            val types = PropertyTypeDataModel()
            types.id = typeArray.getJSONObject(i).getInt("id")
            types.codevalue = typeArray.getJSONObject(i).getString("codevalue")
            types.icon = typeArray.getJSONObject(i).getString("icon")
            types.codetype = typeArray.getJSONObject(i).getString("codetype")
            types.parent = typeArray.getJSONObject(i).getInt("parent")

            when {
                typeArray.getJSONObject(i).getString("parent") == "1" -> {
                    residentialPropertyArrayList.add(types)
                }
                typeArray.getJSONObject(i).getString("parent") == "2" -> {
                    commercialPropertyArrayList.add(types)
                }
                typeArray.getJSONObject(i).getString("parent") == "3" -> {
                    agriculturalPropertyArrayList.add(types)
                }
                else -> {
                    otherPropertyArrayList.add(types)
                }
            }

            residentialTypeLinkedList["Residential"] = residentialPropertyArrayList
            commercialTypeLinkedList["Commercial"] = commercialPropertyArrayList
            agriculturalTypeLinkedList["Agricultural"] = agriculturalPropertyArrayList

            propertyTypeArrayList.add(types)

            filterInfo.residentialTypeLinkedHashMap = residentialTypeLinkedList
            filterInfo.commercialTypeLinkedList = commercialTypeLinkedList
            filterInfo.agriculturalTypeLinkedList = agriculturalTypeLinkedList
            filterInfo.otherTypeLinkedList = otherTypeLinkedList
        }
        filterInfo.propertytypemaster = propertyTypeArrayList


        val saletype = ArrayList<Arbitragemaster>()
        val sale = jsonObject.getJSONArray("saletypemaster")
        for (i in 0 until sale.length()) {
            val saleMaster = Arbitragemaster()

            saleMaster.id = sale.getJSONObject(i).getInt("id")
            saleMaster.codevalue = sale.getJSONObject(i).getString("codevalue")
            saleMaster.codetype = sale.getJSONObject(i).getString("codetype")
            saleMaster.icon = sale.getJSONObject(i).getString("icon")
            saleMaster.parent = sale.getJSONObject(i).getInt("parent")
            saletype.add(saleMaster)
        }
        filterInfo.saletypemaster = saletype


        /*    val stateArray = ArrayList<Statemaster>()
            val state = jsonObject.getJSONArray("statemaster")
            for (i in 0 until state.length()) {
                val stateMaster = Statemaster()
                stateMaster.id = state.getJSONObject(i).getInt("id")
                stateMaster.state = state.getJSONObject(i).getString("state")
                stateArray.add(stateMaster)
            }
            filterInfo.statemaster = stateArray*/

        return filterInfo
    }

    fun parseAmenitiesResponse(response: String): FilterMasterDataModel {
        val filterInfo = FilterMasterDataModel()
        val jsonObject = JSONObject(response)


        val amenitiesmasterArray = ArrayList<Amenitiesmaster>()
        val amenitiesArray = jsonObject.getJSONArray("Amenitiesmaster")
        for (i in 0 until amenitiesArray.length()) {
            val amenities = Amenitiesmaster()
            amenities.id = amenitiesArray.getJSONObject(i).getInt("id")
            amenities.codevalue = amenitiesArray.getJSONObject(i).getString("codevalue")
            amenities.icon = amenitiesArray.getJSONObject(i).getString("icon")
            amenities.codetype = amenitiesArray.getJSONObject(i).getString("codetype")
            amenities.parent = amenitiesArray.getJSONObject(i).getInt("parent")
            amenitiesmasterArray.add(amenities)
        }
        filterInfo.Amenitiesmaster = amenitiesmasterArray

        val parkingArray = ArrayList<Bedroom>()
        val parking = jsonObject.getJSONArray("parking")
        for (i in 0 until parking.length()) {
            val parkingMaster = Bedroom()
            parkingMaster.id = parking.getJSONObject(i).getInt("id")
            parkingMaster.value = parking.getJSONObject(i).getString("value")
            parkingArray.add(parkingMaster)
        }
        filterInfo.parking = parkingArray

        val bathroomArray = ArrayList<Bedroom>()
        val bathroom = jsonObject.getJSONArray("bathroom")
        for (i in 0 until bathroom.length()) {
            val bathroomMaster = Bedroom()
            bathroomMaster.id = bathroom.getJSONObject(i).getInt("id")
            bathroomMaster.value = bathroom.getJSONObject(i).getString("value")
            bathroomArray.add(bathroomMaster)
        }
        filterInfo.bathroom = bathroomArray

        val balconeyArray = ArrayList<Bedroom>()
        val balconey = jsonObject.getJSONArray("balconey")
        for (i in 0 until balconey.length()) {
            val balconyMaster = Bedroom()
            balconyMaster.id = balconey.getJSONObject(i).getInt("id")
            balconyMaster.value = balconey.getJSONObject(i).getString("value")
            balconeyArray.add(balconyMaster)
        }
        filterInfo.balconey = balconeyArray


        val floormasterArray = ArrayList<Floormaster>()
        val floorArray = jsonObject.getJSONArray("floormaster")
        for (i in 0 until floorArray.length()) {
            val floors = Floormaster()
            floors.floor = floorArray.getJSONObject(i).getString("floor")
            floors.floorvalue = floorArray.getJSONObject(i).getString("floorvalue")
            floormasterArray.add(floors)
        }
        filterInfo.floormaster = floormasterArray


        val furnishedmasterArray = ArrayList<Arbitragemaster>()
        val furnishedArray = jsonObject.getJSONArray("furnishedmaster")
        for (i in 0 until furnishedArray.length()) {
            val furnished = Arbitragemaster()
            furnished.id = furnishedArray.getJSONObject(i).getInt("id")
            furnished.codevalue = furnishedArray.getJSONObject(i).getString("codevalue")
            furnished.icon = furnishedArray.getJSONObject(i).getString("icon")
            furnished.codetype = furnishedArray.getJSONObject(i).getString("codetype")
            furnished.parent = furnishedArray.getJSONObject(i).getInt("parent")
            furnishedmasterArray.add(furnished)
        }
        filterInfo.furnishedmaster = furnishedmasterArray

        return filterInfo
    }

    fun parseMyListingResponse(response: String): ListingDataModel {
        val listingInfo = ListingDataModel()
        val jsonObject = JSONObject(response)


        val featurePropertiesArrayList = ArrayList<FeaturePropertiesDataModel>()
        val propertiesArray = jsonObject.getJSONArray("properties")

        for (i in 0 until propertiesArray.length()) {
            val propertiesList = FeaturePropertiesDataModel()
            propertiesList.id = propertiesArray.getJSONObject(i).getInt("id")
            propertiesList.saletypename =
                propertiesArray.getJSONObject(i).getString("saletypename")
            propertiesList.propertyid = propertiesArray.getJSONObject(i).getString("propertyid")
            propertiesList.projectid = propertiesArray.getJSONObject(i).getString("projectid")
            propertiesList.title = propertiesArray.getJSONObject(i).getString("title")
            propertiesList.description =
                propertiesArray.getJSONObject(i).getString("description")
            propertiesList.bedroom = propertiesArray.getJSONObject(i).getString("bedroom")
            propertiesList.bathroom = propertiesArray.getJSONObject(i).getString("bathroom")
            propertiesList.totalarea = propertiesArray.getJSONObject(i).getString("totalarea")
            propertiesList.categoryname =
                propertiesArray.getJSONObject(i).getString("categoryname")
            propertiesList.subcategoryname =
                propertiesArray.getJSONObject(i).getString("subcategoryname")
            propertiesList.possessionname =
                propertiesArray.getJSONObject(i).getString("possessionname")
            propertiesList.cityname = propertiesArray.getJSONObject(i).getString("cityname")
            propertiesList.costinword = propertiesArray.getJSONObject(i).getString("costinword")
            propertiesList.image1 = propertiesArray.getJSONObject(i).getString("image1")
            propertiesList.isverified = propertiesArray.getJSONObject(i).getString("isverified")
            propertiesList.verfiediconpath =
                propertiesArray.getJSONObject(i).getString("verfiediconpath")
            propertiesList.link = propertiesArray.getJSONObject(i).getString("link")
            propertiesList.customertype =
                propertiesArray.getJSONObject(i).getString("customertype")
            propertiesList.addedbyid = propertiesArray.getJSONObject(i).getInt("addedbyid")
            propertiesList.name = propertiesArray.getJSONObject(i).getString("name")
            propertiesList.profilepic = propertiesArray.getJSONObject(i).getString("profilepic")
            propertiesList.badges = propertiesArray.getJSONObject(i).getString("badges")
            propertiesList.strength = propertiesArray.getJSONObject(i).getDouble("strength")
            propertiesList.strengthmsg =
                propertiesArray.getJSONObject(i).getString("strengthmsg")
            propertiesList.address = propertiesArray.getJSONObject(i).getString("address")
            propertiesList.rating = propertiesArray.getJSONObject(i).getDouble("rating")
            propertiesList.lat = propertiesArray.getJSONObject(i).getString("lat")
            propertiesList.lon = propertiesArray.getJSONObject(i).getString("lon")
            propertiesList.startdate = propertiesArray.getJSONObject(i).getString("startdate")
            propertiesList.enddate = propertiesArray.getJSONObject(i).getString("enddate")
            propertiesList.clicks = propertiesArray.getJSONObject(i).getInt("clicks")
            propertiesList.leads = propertiesArray.getJSONObject(i).getInt("leads")
            propertiesList.isfavourite = propertiesArray.getJSONObject(i).getString("isfavourite")
            featurePropertiesArrayList.add(propertiesList)
        }
        listingInfo.featuredproperties = featurePropertiesArrayList


        val projectsArrayList = ArrayList<ProjectsDataModel>()
        val projectsArray = jsonObject.getJSONArray("projects")

        for (i in 0 until projectsArray.length()) {
            val projects = ProjectsDataModel()
            projects.id = projectsArray.getJSONObject(i).getInt("id")
            projects.projectname =
                projectsArray.getJSONObject(i).getString("projectname")
            projects.Fulladdress = projectsArray.getJSONObject(i).getString("Fulladdress")
            projects.projectid = projectsArray.getJSONObject(i).getString("projectid")
            projects.mincost = projectsArray.getJSONObject(i).getString("mincost")
            projects.description =
                projectsArray.getJSONObject(i).getString("ddescription")
            projects.maxcost = projectsArray.getJSONObject(i).getString("maxcost")
            projects.configuration = projectsArray.getJSONObject(i).getString("configuration")
            projects.configuration2 = projectsArray.getJSONObject(i).getString("configuration2")
            projects.categoryname =
                projectsArray.getJSONObject(i).getString("categoryname")
            projects.subcategoryname =
                projectsArray.getJSONObject(i).getString("subcategoryname")
            projects.possessionname =
                projectsArray.getJSONObject(i).getString("possessionname")
            projects.image1 = projectsArray.getJSONObject(i).getString("image1")
            projects.customertype =
                projectsArray.getJSONObject(i).getString("customertype")
            projects.addedbyid = projectsArray.getJSONObject(i).getInt("addedbyid")
            projects.name = projectsArray.getJSONObject(i).getString("name")
            projects.strength = projectsArray.getJSONObject(i).getDouble("strength")
            projects.strengthmsg = projectsArray.getJSONObject(i).getString("strengthmsg")
            projects.rating = projectsArray.getJSONObject(i).getDouble("rating")
            projects.lat = projectsArray.getJSONObject(i).getString("lat")
            projects.lon = projectsArray.getJSONObject(i).getString("lon")
            projects.startdate = projectsArray.getJSONObject(i).getString("startdate")
            projects.enddate = projectsArray.getJSONObject(i).getString("enddate")
            projects.clicks = projectsArray.getJSONObject(i).getInt("clicks")
            projects.leads = projectsArray.getJSONObject(i).getInt("leads")
            projects.isfavourite = projectsArray.getJSONObject(i).getString("isfavourite")
            projects.isverfiedicon = projectsArray.getJSONObject(i).getString("isverfiedicon")
            projects.linkurl = projectsArray.getJSONObject(i).getString("linkurl")
            projects.badges = projectsArray.getJSONObject(i).getString("badges")
            projects.profilepic = projectsArray.getJSONObject(i).getString("profilepic")

            projectsArrayList.add(projects)
        }
        listingInfo.projects = projectsArrayList


        return listingInfo
    }


    fun parseNewsList(response: String): ArrayList<NewsDataModelItem> {
        val newsList = ArrayList<NewsDataModelItem>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val news = NewsDataModelItem()
            news.authoremail = jsonArray.getJSONObject(i).getString("authoremail")
            news.authorname = jsonArray.getJSONObject(i).getString("authorname")
            news.content = jsonArray.getJSONObject(i).getString("content")
            news.link = jsonArray.getJSONObject(i).getString("link")
            news.published = jsonArray.getJSONObject(i).getString("published")
            news.title = jsonArray.getJSONObject(i).getString("title")

            newsList.add(news)
        }
        return newsList
    }

    fun parseMyActivityList(response: String): ArrayList<ActivityDataModel> {
        val activityList = ArrayList<ActivityDataModel>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val activity = ActivityDataModel()
            activity.Image1 = jsonArray.getJSONObject(i).getString("Image1")
            activity.dated = jsonArray.getJSONObject(i).getString("dated")
            activity.narration = jsonArray.getJSONObject(i).getString("narration")
            activity.link = jsonArray.getJSONObject(i).getString("link")
            activity.pagename = jsonArray.getJSONObject(i).getString("pagename")
            activity.title = jsonArray.getJSONObject(i).getString("title")

            activityList.add(activity)
        }
        return activityList
    }

    fun parseChatBoxList(response: String): ArrayList<ChatBoxDataModel> {
        val chatBoxList = ArrayList<ChatBoxDataModel>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val chatBox = ChatBoxDataModel()
            chatBox.address = jsonArray.getJSONObject(i).getString("address")
            chatBox.dated = jsonArray.getJSONObject(i).getString("dated")
            chatBox.agencyname = jsonArray.getJSONObject(i).getString("agencyname")
            chatBox.arbitrage = jsonArray.getJSONObject(i).getString("arbitrage")
            chatBox.badge = jsonArray.getJSONObject(i).getString("badge")
            chatBox.title = jsonArray.getJSONObject(i).getString("title")
            chatBox.categoryname = jsonArray.getJSONObject(i).getString("categoryname")
            chatBox.configuration = jsonArray.getJSONObject(i).getString("configuration")
            chatBox.cost = jsonArray.getJSONObject(i).getString("cost")
            chatBox.iconimage = jsonArray.getJSONObject(i).getString("iconimage")
            chatBox.saletype = jsonArray.getJSONObject(i).getString("saletype")
            chatBox.name = jsonArray.getJSONObject(i).getString("name")
            chatBox.subcategoryname = jsonArray.getJSONObject(i).getString("subcategoryname")
            chatBox.type = jsonArray.getJSONObject(i).getString("type")
            chatBox.longid = jsonArray.getJSONObject(i).getString("longid")
            chatBox.cid = jsonArray.getJSONObject(i).getInt("cid")
            chatBox.id = jsonArray.getJSONObject(i).getInt("id")
            chatBoxList.add(chatBox)
        }
        return chatBoxList
    }

    fun parseSuperGroupsList(response: String): ArrayList<SuperGroupsDataModelItem> {
        val superGroupsListList = ArrayList<SuperGroupsDataModelItem>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val superGroupsList = SuperGroupsDataModelItem()

            superGroupsList.title = jsonArray.getJSONObject(i).getString("title")
            superGroupsList.threadid = jsonArray.getJSONObject(i).getString("threadid")
            superGroupsList.subcategoryname =
                jsonArray.getJSONObject(i).getString("subcategoryname")
            superGroupsList.subcategory = jsonArray.getJSONObject(i).getInt("subcategory")
            superGroupsList.saletypename = jsonArray.getJSONObject(i).getString("saletypename")
            superGroupsList.saletype = jsonArray.getJSONObject(i).getInt("saletype")
            superGroupsList.priceinwords = jsonArray.getJSONObject(i).getString("priceinwords")
            superGroupsList.price = jsonArray.getJSONObject(i).getDouble("price")
            superGroupsList.postedsince = jsonArray.getJSONObject(i).getString("postedsince")
            superGroupsList.saletype = jsonArray.getJSONObject(i).getInt("saletype")
            superGroupsList.possessionname = jsonArray.getJSONObject(i).getString("possessionname")
            superGroupsList.possession = jsonArray.getJSONObject(i).getInt("possession")
            superGroupsList.lon = jsonArray.getJSONObject(i).getString("lon")
            superGroupsList.locality = jsonArray.getJSONObject(i).getString("locality")
            superGroupsList.link = jsonArray.getJSONObject(i).getString("link")
            superGroupsList.likes = jsonArray.getJSONObject(i).getInt("likes")
            superGroupsList.lat = jsonArray.getJSONObject(i).getString("lat")
            superGroupsList.id = jsonArray.getJSONObject(i).getInt("id")
            superGroupsList.dislikes = jsonArray.getJSONObject(i).getInt("dislikes")
            superGroupsList.description = jsonArray.getJSONObject(i).getString("description")
            superGroupsList.contactname = jsonArray.getJSONObject(i).getString("contactname")
            superGroupsList.comment = jsonArray.getJSONObject(i).getInt("comment")
            superGroupsList.categoryname = jsonArray.getJSONObject(i).getString("categoryname")
            superGroupsList.category = jsonArray.getJSONObject(i).getInt("category")
            superGroupsList.arbitragename = jsonArray.getJSONObject(i).getString("arbitragename")
            superGroupsList.arbitragebadge = jsonArray.getJSONObject(i).getString("arbitragebadge")
            superGroupsList.arbitrage = jsonArray.getJSONObject(i).getInt("arbitrage")
            superGroupsList.agencyname = jsonArray.getJSONObject(i).getString("agencyname")
            superGroupsList.addedby = jsonArray.getJSONObject(i).getInt("addedby")
            superGroupsList.Viewed = jsonArray.getJSONObject(i).getInt("Viewed")
            superGroupsListList.add(superGroupsList)
        }
        return superGroupsListList
    }

    fun parseChatUsersList(response: String): ArrayList<ChatUsersDataModel> {
        val chatUserList = ArrayList<ChatUsersDataModel>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val chatUsers = ChatUsersDataModel()
            chatUsers.loginstatus = jsonArray.getJSONObject(i).getString("loginstatus")
            chatUsers.logo = jsonArray.getJSONObject(i).getString("logo")
            chatUsers.agencyname = jsonArray.getJSONObject(i).getString("agencyname")
            chatUsers.name = jsonArray.getJSONObject(i).getString("name")
            chatUsers.recentchat = jsonArray.getJSONObject(i).getString("recentchat")
            chatUsers.id = jsonArray.getJSONObject(i).getInt("id")
            chatUsers.recentchatid = jsonArray.getJSONObject(i).getInt("recentchatid")
            chatUsers.recentcommentid = jsonArray.getJSONObject(i).getInt("recentcommentid")
            chatUsers.rating = jsonArray.getJSONObject(i).getDouble("rating")

            chatUserList.add(chatUsers)
        }
        return chatUserList
    }


    fun parseShortUrlList(response: String): ArrayList<ShortUrlDataModelItem> {
        val shortUrlList = ArrayList<ShortUrlDataModelItem>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val shortUrl = ShortUrlDataModelItem()
            shortUrl.dated = jsonArray.getJSONObject(i).getString("dated")
            shortUrl.rurl = jsonArray.getJSONObject(i).getString("rurl")
            shortUrl.shorturl = jsonArray.getJSONObject(i).getString("shorturl")
            shortUrl.visitcount = jsonArray.getJSONObject(i).getInt("visitcount")
            shortUrl.id = jsonArray.getJSONObject(i).getInt("id")


            shortUrlList.add(shortUrl)
        }
        return shortUrlList
    }

    fun parseSavedSearchesList(response: String): ArrayList<SavedSearchDataModelItem> {
        val savedSearchesList = ArrayList<SavedSearchDataModelItem>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val savedSearches = SavedSearchDataModelItem()
            savedSearches.Link = jsonArray.getJSONObject(i).getString("Link")
            savedSearches.id = jsonArray.getJSONObject(i).getInt("id")
            savedSearches.searchname = jsonArray.getJSONObject(i).getString("searchname")
            savedSearches.searchtype = jsonArray.getJSONObject(i).getString("searchtype")
            savedSearches.status = jsonArray.getJSONObject(i).getString("status")
            savedSearches.userid = jsonArray.getJSONObject(i).getString("userid")

            savedSearchesList.add(savedSearches)
        }
        return savedSearchesList
    }


    fun parseUserProfileResponse(response: String): ArrayList<UserProfileDataModel> {
        val userProfile = ArrayList<UserProfileDataModel>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val user = UserProfileDataModel()
            user.userid = jsonArray.getJSONObject(i).getInt("userid")
            user.scode = jsonArray.getJSONObject(i).getString("scode")
            user.customertype = jsonArray.getJSONObject(i).getString("customertype")
            user.name = jsonArray.getJSONObject(i).getString("name")
            user.mobile = jsonArray.getJSONObject(i).getString("mobile")
            user.email = jsonArray.getJSONObject(i).getString("email")
            user.password = jsonArray.getJSONObject(i).getString("password")
            user.country = jsonArray.getJSONObject(i).getInt("country")
            user.state = jsonArray.getJSONObject(i).getInt("state")
            user.city = jsonArray.getJSONObject(i).getInt("city")
            user.address = jsonArray.getJSONObject(i).getString("address")
            user.statename = jsonArray.getJSONObject(i).getString("statename")
            user.cityname = jsonArray.getJSONObject(i).getString("cityname")
            user.agencyname = jsonArray.getJSONObject(i).getString("agencyname")
            user.description = jsonArray.getJSONObject(i).getString("description")
            user.customer_link = jsonArray.getJSONObject(i).getString("customer_link")
            user.profilepic = jsonArray.getJSONObject(i).getString("profilepic")
            user.lat = jsonArray.getJSONObject(i).getString("lat")
            user.lon = jsonArray.getJSONObject(i).getString("lon")
            user.badges = jsonArray.getJSONObject(i).getString("badges")
            user.rating = jsonArray.getJSONObject(i).getDouble("rating")
            user.userstatus = jsonArray.getJSONObject(i).getString("userstatus")
            user.strength = jsonArray.getJSONObject(i).getDouble("strength")

            userProfile.add(user)
        }
        return userProfile
    }

    fun parseLeadsResponse(response: String): ArrayList<LeadsDataModel> {
        val leadsArray = ArrayList<LeadsDataModel>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val lead = LeadsDataModel()
            lead.Image1 = jsonArray.getJSONObject(i).getString("Image1")
            lead.userid = jsonArray.getJSONObject(i).getString("userid")
            lead.type = jsonArray.getJSONObject(i).getString("type")
            lead.title = jsonArray.getJSONObject(i).getString("title")
            lead.name = jsonArray.getJSONObject(i).getString("name")
            lead.mobile = jsonArray.getJSONObject(i).getString("mobile")
            lead.email = jsonArray.getJSONObject(i).getString("email")
            lead.propertyid = jsonArray.getJSONObject(i).getString("propertyid")
            lead.logo = jsonArray.getJSONObject(i).getString("logo")
            lead.link = jsonArray.getJSONObject(i).getString("link")
            lead.dated = jsonArray.getJSONObject(i).getString("dated")
            lead.agencyname = jsonArray.getJSONObject(i).getString("agencyname")
            lead.cost = jsonArray.getJSONObject(i).getString("cost")
            lead.customer_link = jsonArray.getJSONObject(i).getString("customer_link")
            lead.badgecodes = jsonArray.getJSONObject(i).getString("badgecodes")
            lead.agencyname = jsonArray.getJSONObject(i).getString("agencyname")
            lead.badges = jsonArray.getJSONObject(i).getString("badges")
            lead.addedbyid = jsonArray.getJSONObject(i).getInt("addedbyid")
            lead.chatid = jsonArray.getJSONObject(i).getInt("chatid")
            leadsArray.add(lead)
        }
        return leadsArray
    }

    fun parseNotificationsResponse(response: String): ArrayList<NotificationsDataModelItem> {
        val notificationsArray = ArrayList<NotificationsDataModelItem>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {

            val notification = NotificationsDataModelItem()
            notification.CreatedDate = jsonArray.getJSONObject(i).getString("CreatedDate")
            notification.Description = jsonArray.getJSONObject(i).getString("Description")
            notification.NotificationFrom = jsonArray.getJSONObject(i).getString("NotificationFrom")
            notification.NotificationTo = jsonArray.getJSONObject(i).getString("NotificationTo")
            notification.NotificationType = jsonArray.getJSONObject(i).getString("NotificationType")
            notification.NotificatonTitle = jsonArray.getJSONObject(i).getString("NotificatonTitle")
            notification.RelatedID = jsonArray.getJSONObject(i).getInt("RelatedID")
            notification.Status = jsonArray.getJSONObject(i).getString("Status")
            notification.deviceid = jsonArray.getJSONObject(i).getString("deviceid")
            notification.id = jsonArray.getJSONObject(i).getInt("id")

            notificationsArray.add(notification)
        }
        return notificationsArray
    }

    fun parseProjectDetailResponse(response: String): ProjectDetailDataModel {
        val projectInfo = ProjectDetailDataModel()
        val jsonObject = JSONObject(response)

        val additionalInfoModel = AdditionaldetailsProject()
        val additionalDetail = jsonObject.getString("additionaldetails")
        val additionalDetailJsonObj = JSONObject(additionalDetail)

        additionalInfoModel.isreraverified = additionalDetailJsonObj.getString("isreraverified")
        additionalInfoModel.launchdate = additionalDetailJsonObj.getString("launchdate")
        additionalInfoModel.possessionstartdate =
            additionalDetailJsonObj.getString("possessionstartdate")
        additionalInfoModel.projectarea = additionalDetailJsonObj.getDouble("projectarea")
        additionalInfoModel.rera = additionalDetailJsonObj.getString("rera")
        additionalInfoModel.wscore = additionalDetailJsonObj.getString("wscore")
        additionalInfoModel.wslink = additionalDetailJsonObj.getString("wslink")
        additionalInfoModel.totaltowers =
            additionalDetailJsonObj.getInt("totaltowers")
        additionalInfoModel.wsdesc = additionalDetailJsonObj.getString("wsdesc")
        additionalInfoModel.totalunits = additionalDetailJsonObj.getInt("totalunits")

        projectInfo.additionaldetails = additionalInfoModel


        val projects = ProjectsDataModel()
        val projectsDetail = jsonObject.getString("details")
        val projecJsonObject = JSONObject(projectsDetail)

        projects.id = projecJsonObject.getInt("id")
        projects.projectname =
            projecJsonObject.getString("projectname")
        projects.Fulladdress = projecJsonObject.getString("Fulladdress")
        projects.projectid = projecJsonObject.getString("projectid")
        projects.mincost = projecJsonObject.getString("mincost")
        projects.description =
            projecJsonObject.getString("ddescription")
        projects.maxcost = projecJsonObject.getString("maxcost")
        projects.configuration = projecJsonObject.getString("configuration")
        projects.configuration2 = projecJsonObject.getString("configuration2")
        projects.categoryname =
            projecJsonObject.getString("categoryname")
        projects.subcategoryname =
            projecJsonObject.getString("subcategoryname")
        projects.possessionname =
            projecJsonObject.getString("possessionname")
        projects.image1 = projecJsonObject.getString("image1")
        projects.customertype =
            projecJsonObject.getString("customertype")
        projects.addedbyid = projecJsonObject.getInt("addedbyid")
        projects.name = projecJsonObject.getString("name")
        projects.strength = projecJsonObject.getDouble("strength")
        projects.strengthmsg = projecJsonObject.getString("strengthmsg")
        projects.rating = projecJsonObject.getDouble("rating")
        projects.lat = projecJsonObject.getString("lat")
        projects.lon = projecJsonObject.getString("lon")
        projects.isfavourite = projecJsonObject.getString("isfavourite")
        projects.linkurl = projecJsonObject.getString("linkurl")
        projects.badges = projecJsonObject.getString("badges")
        projects.profilepic = projecJsonObject.getString("profilepic")

        projectInfo.details = projects


        val amenitiesmasterArray = ArrayList<Amenitiesmaster>()
        val amenitiesArray = jsonObject.getJSONArray("Amenitiesmasters")
        for (i in 0 until amenitiesArray.length()) {
            val amenities = Amenitiesmaster()
            amenities.id = amenitiesArray.getJSONObject(i).getInt("id")
            amenities.codevalue = amenitiesArray.getJSONObject(i).getString("codevalue")
            amenities.icon = amenitiesArray.getJSONObject(i).getString("icon")
            amenities.codetype = amenitiesArray.getJSONObject(i).getString("codetype")
            amenities.parent = amenitiesArray.getJSONObject(i).getInt("parent")
            amenitiesmasterArray.add(amenities)
        }
        projectInfo.Amenitiesmasters = amenitiesmasterArray


        val configurationMasterArray = ArrayList<Projectconfiguration>()
        val configurationArray = jsonObject.getJSONArray("projectconfiguration")
        for (i in 0 until configurationArray.length()) {
            val configuration = Projectconfiguration()
            configuration.id = configurationArray.getJSONObject(i).getInt("id")
            configuration.projectid = configurationArray.getJSONObject(i).getInt("projectid")
            configuration.uimage = configurationArray.getJSONObject(i).getString("uimage")
            configuration.uprice = configurationArray.getJSONObject(i).getString("uprice")
            configuration.uprice2 = configurationArray.getJSONObject(i).getString("uprice2")
            configuration.usize = configurationArray.getJSONObject(i).getString("usize")
            configuration.utype = configurationArray.getJSONObject(i).getString("utype")
            configurationMasterArray.add(configuration)
        }
        projectInfo.projectconfiguration = configurationMasterArray

        val advertismentmasterArray = ArrayList<Advertisment>()
        val advertiseArray = jsonObject.getJSONArray("advertisment")
        for (i in 0 until advertiseArray.length()) {
            val advertise = Advertisment()

            advertise.id = advertiseArray.getJSONObject(i).getInt("id")
            advertise.title = advertiseArray.getJSONObject(i).getString("title")
            advertise.statename = advertiseArray.getJSONObject(i).getString("statename")
            advertise.sdate = advertiseArray.getJSONObject(i).getString("sdate")
            advertise.property = advertiseArray.getJSONObject(i).getInt("property")
            advertise.project = advertiseArray.getJSONObject(i).getInt("project")
            advertise.placement = advertiseArray.getJSONObject(i).getString("placement")
            advertise.locality = advertiseArray.getJSONObject(i).getString("locality")
            advertise.link = advertiseArray.getJSONObject(i).getString("link")
            advertise.image = advertiseArray.getJSONObject(i).getString("image")
            advertise.edate = advertiseArray.getJSONObject(i).getString("edate")
            advertise.cityname = advertiseArray.getJSONObject(i).getString("cityname")
            advertise.badges = advertiseArray.getJSONObject(i).getString("badges")
            advertise.address = advertiseArray.getJSONObject(i).getString("address")
            advertise.addtype = advertiseArray.getJSONObject(i).getString("addtype")
            advertise.agency = advertiseArray.getJSONObject(i).getInt("agency")
            advertise.agent = advertiseArray.getJSONObject(i).getInt("agent")
            advertismentmasterArray.add(advertise)
        }
        projectInfo.advertisment = advertismentmasterArray

        val distancefromlocationmasterArray = ArrayList<Distancefromlocation>()
        val distancefromlocationArray = jsonObject.getJSONArray("distancefromlocation")
        for (i in 0 until distancefromlocationArray.length()) {
            val distancefromlocation = Distancefromlocation()

            distancefromlocation.id = distancefromlocationArray.getJSONObject(i).getInt("id")
            distancefromlocation.title =
                distancefromlocationArray.getJSONObject(i).getString("title")
            distancefromlocation.distancetime =
                distancefromlocationArray.getJSONObject(i).getString("distancetime")
            distancefromlocation.distance =
                distancefromlocationArray.getJSONObject(i).getString("distance")
            distancefromlocation.address =
                distancefromlocationArray.getJSONObject(i).getString("address")
            distancefromlocationmasterArray.add(distancefromlocation)
        }
        projectInfo.distancefromlocation = distancefromlocationmasterArray


        val projectsArrayList = ArrayList<ProjectsDataModel>()
        val projectsArray = jsonObject.getJSONArray("featuredprojects")
        for (i in 0 until projectsArray.length()) {
            val projects = ProjectsDataModel()
            projects.id = projectsArray.getJSONObject(i).getInt("id")
            projects.projectname =
                projectsArray.getJSONObject(i).getString("projectname")
            projects.Fulladdress = projectsArray.getJSONObject(i).getString("Fulladdress")
            projects.projectid = projectsArray.getJSONObject(i).getString("projectid")
            projects.mincost = projectsArray.getJSONObject(i).getString("mincost")
            projects.description =
                projectsArray.getJSONObject(i).getString("ddescription")
            projects.maxcost = projectsArray.getJSONObject(i).getString("maxcost")
            projects.configuration = projectsArray.getJSONObject(i).getString("configuration")
            projects.configuration2 = projectsArray.getJSONObject(i).getString("configuration2")
            projects.categoryname =
                projectsArray.getJSONObject(i).getString("categoryname")
            projects.subcategoryname =
                projectsArray.getJSONObject(i).getString("subcategoryname")
            projects.possessionname =
                projectsArray.getJSONObject(i).getString("possessionname")
            projects.image1 = projectsArray.getJSONObject(i).getString("image1")
            projects.customertype =
                projectsArray.getJSONObject(i).getString("customertype")
            projects.addedbyid = projectsArray.getJSONObject(i).getInt("addedbyid")
            projects.name = projectsArray.getJSONObject(i).getString("name")
            projects.strength = projectsArray.getJSONObject(i).getDouble("strength")
            projects.strengthmsg = projectsArray.getJSONObject(i).getString("strengthmsg")
            projects.rating = projectsArray.getJSONObject(i).getDouble("rating")
            projects.lat = projectsArray.getJSONObject(i).getString("lat")
            projects.lon = projectsArray.getJSONObject(i).getString("lon")
            projects.isfavourite = projectsArray.getJSONObject(i).getString("isfavourite")
            projects.linkurl = projectsArray.getJSONObject(i).getString("linkurl")
            projects.badges = projectsArray.getJSONObject(i).getString("badges")
            projects.profilepic = projectsArray.getJSONObject(i).getString("profilepic")

            projectsArrayList.add(projects)
        }
        projectInfo.featuredprojects = projectsArrayList


        val propertyimagesmasterArray = ArrayList<Propertyimage>()
        val propertyimagesArray = jsonObject.getJSONArray("projectimages")
        for (i in 0 until propertyimagesArray.length()) {
            val propertyimages = Propertyimage()


            propertyimages.id = propertyimagesArray.getJSONObject(i).getInt("id")
            propertyimages.propertyid = propertyimagesArray.getJSONObject(i).getInt("projectid")
            propertyimages.imagepath = propertyimagesArray.getJSONObject(i).getString("imagepath")
            propertyimages.imagetype = propertyimagesArray.getJSONObject(i).getString("imagetype")

            propertyimagesmasterArray.add(propertyimages)
        }
        projectInfo.projectimages = propertyimagesmasterArray

        val reviewmasterArray = ArrayList<Review>()
        val reviewArray = jsonObject.getJSONArray("review")
        for (i in 0 until reviewArray.length()) {
            val review = Review()

            review.logo = reviewArray.getJSONObject(i).getString("logo")
            review.name = reviewArray.getJSONObject(i).getString("name")
            review.reviewmsg = reviewArray.getJSONObject(i).getString("reviewmsg")
            review.star = reviewArray.getJSONObject(i).getInt("star")
            review.userid = reviewArray.getJSONObject(i).getInt("userid")
            review.userprofile = reviewArray.getJSONObject(i).getString("userprofile")
            reviewmasterArray.add(review)
        }
        projectInfo.review = reviewmasterArray


        val similiarprojectsArrayList = ArrayList<ProjectsDataModel>()
        val similiarprojectsArray = jsonObject.getJSONArray("similarprojects")
        for (i in 0 until similiarprojectsArray.length()) {
            val similarprojects = ProjectsDataModel()
            similarprojects.id = similiarprojectsArray.getJSONObject(i).getInt("id")
            similarprojects.projectname =
                similiarprojectsArray.getJSONObject(i).getString("projectname")
            similarprojects.Fulladdress =
                similiarprojectsArray.getJSONObject(i).getString("Fulladdress")
            similarprojects.projectid =
                similiarprojectsArray.getJSONObject(i).getString("projectid")
            similarprojects.mincost = similiarprojectsArray.getJSONObject(i).getString("mincost")
            similarprojects.description =
                similiarprojectsArray.getJSONObject(i).getString("ddescription")
            similarprojects.maxcost = similiarprojectsArray.getJSONObject(i).getString("maxcost")
            similarprojects.configuration =
                similiarprojectsArray.getJSONObject(i).getString("configuration")
            similarprojects.configuration2 =
                similiarprojectsArray.getJSONObject(i).getString("configuration2")
            similarprojects.categoryname =
                similiarprojectsArray.getJSONObject(i).getString("categoryname")
            similarprojects.subcategoryname =
                similiarprojectsArray.getJSONObject(i).getString("subcategoryname")
            similarprojects.possessionname =
                similiarprojectsArray.getJSONObject(i).getString("possessionname")
            similarprojects.image1 = similiarprojectsArray.getJSONObject(i).getString("image1")
            similarprojects.customertype =
                similiarprojectsArray.getJSONObject(i).getString("customertype")
            similarprojects.addedbyid = similiarprojectsArray.getJSONObject(i).getInt("addedbyid")
            similarprojects.name = similiarprojectsArray.getJSONObject(i).getString("name")
            similarprojects.strength = similiarprojectsArray.getJSONObject(i).getDouble("strength")
            similarprojects.strengthmsg =
                similiarprojectsArray.getJSONObject(i).getString("strengthmsg")
            similarprojects.rating = similiarprojectsArray.getJSONObject(i).getDouble("rating")
            similarprojects.lat = similiarprojectsArray.getJSONObject(i).getString("lat")
            similarprojects.lon = similiarprojectsArray.getJSONObject(i).getString("lon")
            similarprojects.isfavourite =
                similiarprojectsArray.getJSONObject(i).getString("isfavourite")
            similarprojects.linkurl = similiarprojectsArray.getJSONObject(i).getString("linkurl")
            similarprojects.badges = similiarprojectsArray.getJSONObject(i).getString("badges")
            similarprojects.profilepic =
                similiarprojectsArray.getJSONObject(i).getString("profilepic")

            similiarprojectsArrayList.add(similarprojects)
        }
        projectInfo.similarprojects = similiarprojectsArrayList

        return projectInfo
    }

    fun parseBasicPropertyDetailResponse(response: String): PropertyDetailsModel {
        val propertyInfo = PropertyDetailsModel()
        val jsonObject = JSONObject(response)

        val detailInfoModel = FeaturePropertiesDataModel()

        val propertyDetail = jsonObject.getString("details")
        val detailJsonObj = JSONObject(propertyDetail)

        detailInfoModel.id = detailJsonObj.getInt("id")
        detailInfoModel.saletypename =
            detailJsonObj.getString("saletypename")
        detailInfoModel.propertyid = detailJsonObj.getString("propertyid")
        detailInfoModel.projectid = detailJsonObj.getString("projectid")
        detailInfoModel.title = detailJsonObj.getString("title")
        detailInfoModel.description =
            detailJsonObj.getString("description")
        detailInfoModel.bedroom = detailJsonObj.getString("bedroom")
        detailInfoModel.bathroom = detailJsonObj.getString("bathroom")
        detailInfoModel.totalarea = detailJsonObj.getString("totalarea")
        detailInfoModel.categoryname =
            detailJsonObj.getString("categoryname")
        detailInfoModel.subcategoryname =
            detailJsonObj.getString("subcategoryname")
        detailInfoModel.possessionname =
            detailJsonObj.getString("possessionname")
        detailInfoModel.cityname = detailJsonObj.getString("cityname")
        detailInfoModel.costinword = detailJsonObj.getString("costinword")
        detailInfoModel.image1 = detailJsonObj.getString("image1")
        detailInfoModel.isverified = detailJsonObj.getString("isverified")
        detailInfoModel.verfiediconpath =
            detailJsonObj.getString("verfiediconpath")
        detailInfoModel.link = detailJsonObj.getString("link")
        detailInfoModel.customertype =
            detailJsonObj.getString("customertype")
        detailInfoModel.addedbyid = detailJsonObj.getInt("addedbyid")
        detailInfoModel.name = detailJsonObj.getString("name")
        detailInfoModel.profilepic = detailJsonObj.getString("profilepic")
        detailInfoModel.badges = detailJsonObj.getString("badges")
        detailInfoModel.strength = detailJsonObj.getDouble("strength")
        detailInfoModel.strengthmsg =
            detailJsonObj.getString("strengthmsg")
        detailInfoModel.address = detailJsonObj.getString("address")
        detailInfoModel.rating = detailJsonObj.getDouble("rating")
        detailInfoModel.lat = detailJsonObj.getString("lat")
        detailInfoModel.lon = detailJsonObj.getString("lon")
        detailInfoModel.isfavourite =
            detailJsonObj.getString("isfavourite")

        propertyInfo.details = detailInfoModel

        val additionalInfoModel = Additionaldetails()

        val additionalDetail = jsonObject.getString("additionaldetails")
        val additionalDetailJsonObj = JSONObject(additionalDetail)
        additionalInfoModel.Additionalroom = additionalDetailJsonObj.getString("Additionalroom")
        additionalInfoModel.anyconstruction = additionalDetailJsonObj.getString("anyconstruction")
        additionalInfoModel.attachedbathroom = additionalDetailJsonObj.getInt("attachedbathroom")
        additionalInfoModel.balcony = additionalDetailJsonObj.getInt("balcony")
        additionalInfoModel.boundrywall = additionalDetailJsonObj.getString("boundrywall")
        additionalInfoModel.costperft = additionalDetailJsonObj.getString("costperft")
        additionalInfoModel.depositper = additionalDetailJsonObj.getDouble("depositper")
        additionalInfoModel.expirydate = additionalDetailJsonObj.getString("expirydate")
        additionalInfoModel.floorforconstruction =
            additionalDetailJsonObj.getInt("floorforconstruction")
        additionalInfoModel.iscorner = additionalDetailJsonObj.getString("iscorner")
        additionalInfoModel.isingatedcolony = additionalDetailJsonObj.getString("isingatedcolony")
        additionalInfoModel.isreraverified = additionalDetailJsonObj.getString("isreraverified")
        additionalInfoModel.parking = additionalDetailJsonObj.getInt("parking")
        additionalInfoModel.lastremodalyear = additionalDetailJsonObj.getString("lastremodalyear")
        additionalInfoModel.poolsize = additionalDetailJsonObj.getString("poolsize")
        additionalInfoModel.rera = additionalDetailJsonObj.getString("rera")
        additionalInfoModel.roadfacing = additionalDetailJsonObj.getString("roadfacing")
        additionalInfoModel.wscore = additionalDetailJsonObj.getString("wscore")
        additionalInfoModel.wsdesc = additionalDetailJsonObj.getString("wsdesc")
        additionalInfoModel.wslink = additionalDetailJsonObj.getString("wslink")
        additionalInfoModel.floorno = additionalDetailJsonObj.getString("floorno")
        additionalInfoModel.totalarea2 = additionalDetailJsonObj.getString("totalarea2")
        additionalInfoModel.totalfloor = additionalDetailJsonObj.getInt("totalfloor")

        propertyInfo.additionaldetails = additionalInfoModel

        return propertyInfo
    }

    fun parsePropertyImagesResponse(response: String): PropertyDetailsModel {
        val propertyInfo = PropertyDetailsModel()
        val jsonObject = JSONObject(response)

        val propertyimagesmasterArray = ArrayList<Propertyimage>()
        val propertyimagesArray = jsonObject.getJSONArray("propertyimages")
        for (i in 0 until propertyimagesArray.length()) {
            val propertyimages = Propertyimage()

            propertyimages.id = propertyimagesArray.getJSONObject(i).getInt("id")
            propertyimages.propertyid = propertyimagesArray.getJSONObject(i).getInt("propertyid")
            propertyimages.imagepath = propertyimagesArray.getJSONObject(i).getString("imagepath")
            propertyimages.imagetype = propertyimagesArray.getJSONObject(i).getString("imagetype")

            propertyimagesmasterArray.add(propertyimages)
        }
        propertyInfo.propertyimages = propertyimagesmasterArray

        return propertyInfo
    }

    fun parsePropertyAdditionalResponse(response: String): PropertyDetailsModel {
        val propertyInfo = PropertyDetailsModel()
        val jsonObject = JSONObject(response)


        val additionalInfoModel = Additionaldetails()

        val additionalDetail = jsonObject.getString("additionaldetails")
        val additionalDetailJsonObj = JSONObject(additionalDetail)
        additionalInfoModel.Additionalroom = additionalDetailJsonObj.getString("Additionalroom")
        additionalInfoModel.anyconstruction = additionalDetailJsonObj.getString("anyconstruction")
        additionalInfoModel.attachedbathroom = additionalDetailJsonObj.getInt("attachedbathroom")
        additionalInfoModel.balcony = additionalDetailJsonObj.getInt("balcony")
        additionalInfoModel.boundrywall = additionalDetailJsonObj.getString("boundrywall")
        additionalInfoModel.costperft = additionalDetailJsonObj.getString("costperft")
        additionalInfoModel.depositper = additionalDetailJsonObj.getDouble("depositper")
        additionalInfoModel.expirydate = additionalDetailJsonObj.getString("expirydate")
        additionalInfoModel.floorforconstruction =
            additionalDetailJsonObj.getInt("floorforconstruction")
        additionalInfoModel.iscorner = additionalDetailJsonObj.getString("iscorner")
        additionalInfoModel.isingatedcolony = additionalDetailJsonObj.getString("isingatedcolony")
        additionalInfoModel.isreraverified = additionalDetailJsonObj.getString("isreraverified")
        additionalInfoModel.parking = additionalDetailJsonObj.getInt("parking")
        additionalInfoModel.lastremodalyear = additionalDetailJsonObj.getString("lastremodalyear")
        additionalInfoModel.poolsize = additionalDetailJsonObj.getString("poolsize")
        additionalInfoModel.rera = additionalDetailJsonObj.getString("rera")
        additionalInfoModel.roadfacing = additionalDetailJsonObj.getString("roadfacing")
        additionalInfoModel.wscore = additionalDetailJsonObj.getString("wscore")
        additionalInfoModel.wsdesc = additionalDetailJsonObj.getString("wsdesc")
        additionalInfoModel.wslink = additionalDetailJsonObj.getString("wslink")
        additionalInfoModel.floorno = additionalDetailJsonObj.getString("floorno")
        additionalInfoModel.totalarea2 = additionalDetailJsonObj.getString("totalarea2")
        additionalInfoModel.totalfloor = additionalDetailJsonObj.getInt("totalfloor")

        propertyInfo.additionaldetails = additionalInfoModel

        return propertyInfo
    }


    fun parsePropertyDetailResponse(response: String): PropertyDetailsModel {
        val propertyInfo = PropertyDetailsModel()
        val jsonObject = JSONObject(response)

        val amenitiesmasterArray = ArrayList<Amenitiesmaster>()
        val amenitiesArray = jsonObject.getJSONArray("Amenitiesmasters")
        for (i in 0 until amenitiesArray.length()) {
            val amenities = Amenitiesmaster()
            amenities.id = amenitiesArray.getJSONObject(i).getInt("id")
            amenities.codevalue = amenitiesArray.getJSONObject(i).getString("codevalue")
            amenities.icon = amenitiesArray.getJSONObject(i).getString("icon")
            amenities.codetype = amenitiesArray.getJSONObject(i).getString("codetype")
            amenities.parent = amenitiesArray.getJSONObject(i).getInt("parent")
            amenitiesmasterArray.add(amenities)
        }
        propertyInfo.Amenitiesmasters = amenitiesmasterArray

        val additionalInfoModel = Additionaldetails()

        val additionalDetail = jsonObject.getString("additionaldetails")
        val additionalDetailJsonObj = JSONObject(additionalDetail)
        additionalInfoModel.Additionalroom = additionalDetailJsonObj.getString("Additionalroom")
        additionalInfoModel.anyconstruction = additionalDetailJsonObj.getString("anyconstruction")
        additionalInfoModel.attachedbathroom = additionalDetailJsonObj.getInt("attachedbathroom")
        additionalInfoModel.balcony = additionalDetailJsonObj.getInt("balcony")
        additionalInfoModel.boundrywall = additionalDetailJsonObj.getString("boundrywall")
        additionalInfoModel.costperft = additionalDetailJsonObj.getString("costperft")
        additionalInfoModel.depositper = additionalDetailJsonObj.getDouble("depositper")
        additionalInfoModel.expirydate = additionalDetailJsonObj.getString("expirydate")
        additionalInfoModel.floorforconstruction =
            additionalDetailJsonObj.getInt("floorforconstruction")
        additionalInfoModel.iscorner = additionalDetailJsonObj.getString("iscorner")
        additionalInfoModel.isingatedcolony = additionalDetailJsonObj.getString("isingatedcolony")
        additionalInfoModel.isreraverified = additionalDetailJsonObj.getString("isreraverified")
        additionalInfoModel.parking = additionalDetailJsonObj.getInt("parking")
        additionalInfoModel.lastremodalyear = additionalDetailJsonObj.getString("lastremodalyear")
        additionalInfoModel.poolsize = additionalDetailJsonObj.getString("poolsize")
        additionalInfoModel.rera = additionalDetailJsonObj.getString("rera")
        additionalInfoModel.roadfacing = additionalDetailJsonObj.getString("roadfacing")
        additionalInfoModel.wscore = additionalDetailJsonObj.getString("wscore")
        additionalInfoModel.wsdesc = additionalDetailJsonObj.getString("wsdesc")
        additionalInfoModel.wslink = additionalDetailJsonObj.getString("wslink")
        additionalInfoModel.floorno = additionalDetailJsonObj.getString("floorno")
        additionalInfoModel.totalarea2 = additionalDetailJsonObj.getString("totalarea2")
        additionalInfoModel.totalfloor = additionalDetailJsonObj.getInt("totalfloor")

        propertyInfo.additionaldetails = additionalInfoModel


        val detailInfoModel = FeaturePropertiesDataModel()

        val propertyDetail = jsonObject.getString("details")
        val detailJsonObj = JSONObject(propertyDetail)

        detailInfoModel.id = detailJsonObj.getInt("id")
        detailInfoModel.saletypename =
            detailJsonObj.getString("saletypename")
        detailInfoModel.propertyid = detailJsonObj.getString("propertyid")
        detailInfoModel.projectid = detailJsonObj.getString("projectid")
        detailInfoModel.title = detailJsonObj.getString("title")
        detailInfoModel.description =
            detailJsonObj.getString("description")
        detailInfoModel.bedroom = detailJsonObj.getString("bedroom")
        detailInfoModel.bathroom = detailJsonObj.getString("bathroom")
        detailInfoModel.totalarea = detailJsonObj.getString("totalarea")
        detailInfoModel.categoryname =
            detailJsonObj.getString("categoryname")
        detailInfoModel.subcategoryname =
            detailJsonObj.getString("subcategoryname")
        detailInfoModel.possessionname =
            detailJsonObj.getString("possessionname")
        detailInfoModel.cityname = detailJsonObj.getString("cityname")
        detailInfoModel.costinword = detailJsonObj.getString("costinword")
        detailInfoModel.image1 = detailJsonObj.getString("image1")
        detailInfoModel.isverified = detailJsonObj.getString("isverified")
        detailInfoModel.verfiediconpath =
            detailJsonObj.getString("verfiediconpath")
        detailInfoModel.link = detailJsonObj.getString("link")
        detailInfoModel.customertype =
            detailJsonObj.getString("customertype")
        detailInfoModel.addedbyid = detailJsonObj.getInt("addedbyid")
        detailInfoModel.name = detailJsonObj.getString("name")
        detailInfoModel.profilepic = detailJsonObj.getString("profilepic")
        detailInfoModel.badges = detailJsonObj.getString("badges")
        detailInfoModel.strength = detailJsonObj.getDouble("strength")
        detailInfoModel.strengthmsg =
            detailJsonObj.getString("strengthmsg")
        detailInfoModel.address = detailJsonObj.getString("address")
        detailInfoModel.rating = detailJsonObj.getDouble("rating")
        detailInfoModel.lat = detailJsonObj.getString("lat")
        detailInfoModel.lon = detailJsonObj.getString("lon")
        detailInfoModel.isfavourite =
            detailJsonObj.getString("isfavourite")

        propertyInfo.details = detailInfoModel


        val advertismentmasterArray = ArrayList<Advertisment>()
        val advertiseArray = jsonObject.getJSONArray("advertisment")
        for (i in 0 until advertiseArray.length()) {
            val advertise = Advertisment()

            advertise.id = advertiseArray.getJSONObject(i).getInt("id")
            advertise.title = advertiseArray.getJSONObject(i).getString("title")
            advertise.statename = advertiseArray.getJSONObject(i).getString("statename")
            advertise.sdate = advertiseArray.getJSONObject(i).getString("sdate")
            advertise.property = advertiseArray.getJSONObject(i).getInt("property")
            advertise.project = advertiseArray.getJSONObject(i).getInt("project")
            advertise.placement = advertiseArray.getJSONObject(i).getString("placement")
            advertise.locality = advertiseArray.getJSONObject(i).getString("locality")
            advertise.link = advertiseArray.getJSONObject(i).getString("link")
            advertise.image = advertiseArray.getJSONObject(i).getString("image")
            advertise.edate = advertiseArray.getJSONObject(i).getString("edate")
            advertise.cityname = advertiseArray.getJSONObject(i).getString("cityname")
            advertise.badges = advertiseArray.getJSONObject(i).getString("badges")
            advertise.address = advertiseArray.getJSONObject(i).getString("address")
            advertise.addtype = advertiseArray.getJSONObject(i).getString("addtype")
            advertise.agency = advertiseArray.getJSONObject(i).getInt("agency")
            advertise.agent = advertiseArray.getJSONObject(i).getInt("agent")
            advertismentmasterArray.add(advertise)
        }
        propertyInfo.advertisment = advertismentmasterArray


        val distancefromlocationmasterArray = ArrayList<Distancefromlocation>()
        val distancefromlocationArray = jsonObject.getJSONArray("distancefromlocation")
        for (i in 0 until distancefromlocationArray.length()) {
            val distancefromlocation = Distancefromlocation()

            distancefromlocation.id = distancefromlocationArray.getJSONObject(i).getInt("id")
            distancefromlocation.title =
                distancefromlocationArray.getJSONObject(i).getString("title")
            distancefromlocation.distancetime =
                distancefromlocationArray.getJSONObject(i).getString("distancetime")
            distancefromlocation.distance =
                distancefromlocationArray.getJSONObject(i).getString("distance")
            distancefromlocation.address =
                distancefromlocationArray.getJSONObject(i).getString("address")
            distancefromlocationmasterArray.add(distancefromlocation)
        }
        propertyInfo.distancefromlocation = distancefromlocationmasterArray


        val featurePropertiesArrayList = ArrayList<FeaturePropertiesDataModel>()
        val propertiesArray = jsonObject.getJSONArray("featuredproperty")
        for (i in 0 until propertiesArray.length()) {
            val featureProperties = FeaturePropertiesDataModel()
            featureProperties.id = propertiesArray.getJSONObject(i).getInt("id")
            featureProperties.saletypename =
                propertiesArray.getJSONObject(i).getString("saletypename")
            featureProperties.propertyid = propertiesArray.getJSONObject(i).getString("propertyid")
            featureProperties.projectid = propertiesArray.getJSONObject(i).getString("projectid")
            featureProperties.title = propertiesArray.getJSONObject(i).getString("title")
            featureProperties.description =
                propertiesArray.getJSONObject(i).getString("description")
            featureProperties.bedroom = propertiesArray.getJSONObject(i).getString("bedroom")
            featureProperties.bathroom = propertiesArray.getJSONObject(i).getString("bathroom")
            featureProperties.totalarea = propertiesArray.getJSONObject(i).getString("totalarea")
            featureProperties.categoryname =
                propertiesArray.getJSONObject(i).getString("categoryname")
            featureProperties.subcategoryname =
                propertiesArray.getJSONObject(i).getString("subcategoryname")
            featureProperties.possessionname =
                propertiesArray.getJSONObject(i).getString("possessionname")
            featureProperties.cityname = propertiesArray.getJSONObject(i).getString("cityname")
            featureProperties.costinword = propertiesArray.getJSONObject(i).getString("costinword")
            featureProperties.image1 = propertiesArray.getJSONObject(i).getString("image1")
            featureProperties.isverified = propertiesArray.getJSONObject(i).getString("isverified")
            featureProperties.verfiediconpath =
                propertiesArray.getJSONObject(i).getString("verfiediconpath")
            featureProperties.link = propertiesArray.getJSONObject(i).getString("link")
            featureProperties.customertype =
                propertiesArray.getJSONObject(i).getString("customertype")
            featureProperties.addedbyid = propertiesArray.getJSONObject(i).getInt("addedbyid")
            featureProperties.name = propertiesArray.getJSONObject(i).getString("name")
            featureProperties.profilepic = propertiesArray.getJSONObject(i).getString("profilepic")
            featureProperties.badges = propertiesArray.getJSONObject(i).getString("badges")
            featureProperties.strength = propertiesArray.getJSONObject(i).getDouble("strength")
            featureProperties.strengthmsg =
                propertiesArray.getJSONObject(i).getString("strengthmsg")
            featureProperties.address = propertiesArray.getJSONObject(i).getString("address")
            featureProperties.rating = propertiesArray.getJSONObject(i).getDouble("rating")
            featureProperties.lat = propertiesArray.getJSONObject(i).getString("lat")
            featureProperties.lon = propertiesArray.getJSONObject(i).getString("lon")
            featureProperties.isfavourite =
                propertiesArray.getJSONObject(i).getString("isfavourite")
            featurePropertiesArrayList.add(featureProperties)
        }
        propertyInfo.featuredproperty = featurePropertiesArrayList


        val propertyimagesmasterArray = ArrayList<Propertyimage>()
        val propertyimagesArray = jsonObject.getJSONArray("propertyimages")
        for (i in 0 until propertyimagesArray.length()) {
            val propertyimages = Propertyimage()

            propertyimages.id = propertyimagesArray.getJSONObject(i).getInt("id")
            propertyimages.propertyid = propertyimagesArray.getJSONObject(i).getInt("propertyid")
            propertyimages.imagepath = propertyimagesArray.getJSONObject(i).getString("imagepath")
            propertyimages.imagetype = propertyimagesArray.getJSONObject(i).getString("imagetype")

            propertyimagesmasterArray.add(propertyimages)
        }
        propertyInfo.propertyimages = propertyimagesmasterArray


        val reviewmasterArray = ArrayList<Review>()
        val reviewArray = jsonObject.getJSONArray("review")
        for (i in 0 until reviewArray.length()) {
            val review = Review()

            review.logo = reviewArray.getJSONObject(i).getString("logo")
            review.name = reviewArray.getJSONObject(i).getString("name")
            review.reviewmsg = reviewArray.getJSONObject(i).getString("reviewmsg")
            review.star = reviewArray.getJSONObject(i).getInt("star")
            review.userid = reviewArray.getJSONObject(i).getInt("userid")
            review.userprofile = reviewArray.getJSONObject(i).getString("userprofile")
            reviewmasterArray.add(review)
        }
        propertyInfo.review = reviewmasterArray

        val similarpropertyArrayList = ArrayList<FeaturePropertiesDataModel>()
        val similarpropertyArray = jsonObject.getJSONArray("similarproperty")
        for (i in 0 until similarpropertyArray.length()) {
            val similiarProperties = FeaturePropertiesDataModel()
            similiarProperties.id = similarpropertyArray.getJSONObject(i).getInt("id")
            similiarProperties.saletypename =
                similarpropertyArray.getJSONObject(i).getString("saletypename")
            similiarProperties.propertyid =
                similarpropertyArray.getJSONObject(i).getString("propertyid")
            similiarProperties.projectid =
                similarpropertyArray.getJSONObject(i).getString("projectid")
            similiarProperties.title = similarpropertyArray.getJSONObject(i).getString("title")
            similiarProperties.description =
                similarpropertyArray.getJSONObject(i).getString("description")
            similiarProperties.bedroom = similarpropertyArray.getJSONObject(i).getString("bedroom")
            similiarProperties.bathroom =
                similarpropertyArray.getJSONObject(i).getString("bathroom")
            similiarProperties.totalarea =
                similarpropertyArray.getJSONObject(i).getString("totalarea")
            similiarProperties.categoryname =
                similarpropertyArray.getJSONObject(i).getString("categoryname")
            similiarProperties.subcategoryname =
                similarpropertyArray.getJSONObject(i).getString("subcategoryname")
            similiarProperties.possessionname =
                similarpropertyArray.getJSONObject(i).getString("possessionname")
            similiarProperties.cityname =
                similarpropertyArray.getJSONObject(i).getString("cityname")
            similiarProperties.costinword =
                similarpropertyArray.getJSONObject(i).getString("costinword")
            similiarProperties.image1 = similarpropertyArray.getJSONObject(i).getString("image1")
            similiarProperties.isverified =
                similarpropertyArray.getJSONObject(i).getString("isverified")
            similiarProperties.verfiediconpath =
                similarpropertyArray.getJSONObject(i).getString("verfiediconpath")
            similiarProperties.link = similarpropertyArray.getJSONObject(i).getString("link")
            similiarProperties.customertype =
                similarpropertyArray.getJSONObject(i).getString("customertype")
            similiarProperties.addedbyid = similarpropertyArray.getJSONObject(i).getInt("addedbyid")
            similiarProperties.name = similarpropertyArray.getJSONObject(i).getString("name")
            similiarProperties.profilepic =
                similarpropertyArray.getJSONObject(i).getString("profilepic")
            similiarProperties.badges = similarpropertyArray.getJSONObject(i).getString("badges")
            similiarProperties.strength =
                similarpropertyArray.getJSONObject(i).getDouble("strength")
            similiarProperties.strengthmsg =
                similarpropertyArray.getJSONObject(i).getString("strengthmsg")
            similiarProperties.address = similarpropertyArray.getJSONObject(i).getString("address")
            similiarProperties.rating = similarpropertyArray.getJSONObject(i).getDouble("rating")
            similiarProperties.lat = similarpropertyArray.getJSONObject(i).getString("lat")
            similiarProperties.lon = similarpropertyArray.getJSONObject(i).getString("lon")
            similiarProperties.isfavourite =
                similarpropertyArray.getJSONObject(i).getString("isfavourite")
            similarpropertyArrayList.add(similiarProperties)
        }
        propertyInfo.similarproperty = similarpropertyArrayList

        return propertyInfo
    }

    fun parseUserDetailsResponse(response: String): UserDetailDataModel {
        val userDetailInfo = UserDetailDataModel()
        val jsonObject = JSONObject(response)

        val projectsArrayList = ArrayList<ProjectsDataModel>()
        val projectsArray = jsonObject.getJSONArray("Projects")
        for (i in 0 until projectsArray.length()) {
            val projects = ProjectsDataModel()
            projects.id = projectsArray.getJSONObject(i).getInt("id")
            projects.projectname =
                projectsArray.getJSONObject(i).getString("projectname")
            projects.Fulladdress = projectsArray.getJSONObject(i).getString("Fulladdress")
            projects.projectid = projectsArray.getJSONObject(i).getString("projectid")
            projects.mincost = projectsArray.getJSONObject(i).getString("mincost")
            projects.description =
                projectsArray.getJSONObject(i).getString("ddescription")
            projects.maxcost = projectsArray.getJSONObject(i).getString("maxcost")
            projects.configuration = projectsArray.getJSONObject(i).getString("configuration")
            projects.configuration2 = projectsArray.getJSONObject(i).getString("configuration2")
            projects.categoryname =
                projectsArray.getJSONObject(i).getString("categoryname")
            projects.subcategoryname =
                projectsArray.getJSONObject(i).getString("subcategoryname")
            projects.possessionname =
                projectsArray.getJSONObject(i).getString("possessionname")
            projects.image1 = projectsArray.getJSONObject(i).getString("image1")
            projects.customertype =
                projectsArray.getJSONObject(i).getString("customertype")
            projects.addedbyid = projectsArray.getJSONObject(i).getInt("addedbyid")
            projects.name = projectsArray.getJSONObject(i).getString("name")
            projects.strength = projectsArray.getJSONObject(i).getDouble("strength")
            projects.strengthmsg = projectsArray.getJSONObject(i).getString("strengthmsg")
            projects.rating = projectsArray.getJSONObject(i).getDouble("rating")
            projects.lat = projectsArray.getJSONObject(i).getString("lat")
            projects.lon = projectsArray.getJSONObject(i).getString("lon")
            projects.isfavourite = projectsArray.getJSONObject(i).getString("isfavourite")
            projects.linkurl = projectsArray.getJSONObject(i).getString("linkurl")
            projects.badges = projectsArray.getJSONObject(i).getString("badges")
            projects.profilepic = projectsArray.getJSONObject(i).getString("profilepic")

            projectsArrayList.add(projects)
        }
        userDetailInfo.Projects = projectsArrayList


        val featurePropertiesArrayList = ArrayList<FeaturePropertiesDataModel>()
        val propertiesArray = jsonObject.getJSONArray("Properties")
        for (i in 0 until propertiesArray.length()) {
            val propertiesList = FeaturePropertiesDataModel()
            propertiesList.id = propertiesArray.getJSONObject(i).getInt("id")
            propertiesList.saletypename =
                propertiesArray.getJSONObject(i).getString("saletypename")
            propertiesList.propertyid = propertiesArray.getJSONObject(i).getString("propertyid")
            propertiesList.projectid = propertiesArray.getJSONObject(i).getString("projectid")
            propertiesList.title = propertiesArray.getJSONObject(i).getString("title")
            propertiesList.description =
                propertiesArray.getJSONObject(i).getString("description")
            propertiesList.bedroom = propertiesArray.getJSONObject(i).getString("bedroom")
            propertiesList.bathroom = propertiesArray.getJSONObject(i).getString("bathroom")
            propertiesList.totalarea = propertiesArray.getJSONObject(i).getString("totalarea")
            propertiesList.categoryname =
                propertiesArray.getJSONObject(i).getString("categoryname")
            propertiesList.subcategoryname =
                propertiesArray.getJSONObject(i).getString("subcategoryname")
            propertiesList.possessionname =
                propertiesArray.getJSONObject(i).getString("possessionname")
            propertiesList.cityname = propertiesArray.getJSONObject(i).getString("cityname")
            propertiesList.costinword = propertiesArray.getJSONObject(i).getString("costinword")
            propertiesList.image1 = propertiesArray.getJSONObject(i).getString("image1")
            propertiesList.isverified = propertiesArray.getJSONObject(i).getString("isverified")
            propertiesList.verfiediconpath =
                propertiesArray.getJSONObject(i).getString("verfiediconpath")
            propertiesList.link = propertiesArray.getJSONObject(i).getString("link")
            propertiesList.customertype =
                propertiesArray.getJSONObject(i).getString("customertype")
            propertiesList.addedbyid = propertiesArray.getJSONObject(i).getInt("addedbyid")
            propertiesList.name = propertiesArray.getJSONObject(i).getString("name")
            propertiesList.profilepic = propertiesArray.getJSONObject(i).getString("profilepic")
            propertiesList.badges = propertiesArray.getJSONObject(i).getString("badges")
            propertiesList.strength = propertiesArray.getJSONObject(i).getDouble("strength")
            propertiesList.strengthmsg =
                propertiesArray.getJSONObject(i).getString("strengthmsg")
            propertiesList.address = propertiesArray.getJSONObject(i).getString("address")
            propertiesList.rating = propertiesArray.getJSONObject(i).getDouble("rating")
            propertiesList.lat = propertiesArray.getJSONObject(i).getString("lat")
            propertiesList.lon = propertiesArray.getJSONObject(i).getString("lon")
            propertiesList.startdate = propertiesArray.getJSONObject(i).getString("startdate")
            propertiesList.enddate = propertiesArray.getJSONObject(i).getString("enddate")
            propertiesList.clicks = propertiesArray.getJSONObject(i).getInt("clicks")
            propertiesList.leads = propertiesArray.getJSONObject(i).getInt("leads")
            propertiesList.isfavourite = propertiesArray.getJSONObject(i).getString("isfavourite")
            featurePropertiesArrayList.add(propertiesList)
        }
        userDetailInfo.Properties = featurePropertiesArrayList


        val userActivitymasterArray = ArrayList<Useractivity>()
        val userActivityArray = jsonObject.getJSONArray("Useractivities")
        for (i in 0 until userActivityArray.length()) {
            val userActivity = Useractivity()
            userActivity.title = userActivityArray.getJSONObject(i).getString("title")
            userActivity.pagename = userActivityArray.getJSONObject(i).getString("pagename")
            userActivity.narration = userActivityArray.getJSONObject(i).getString("narration")
            userActivity.link = userActivityArray.getJSONObject(i).getString("link")
            userActivity.dated = userActivityArray.getJSONObject(i).getString("dated")
            userActivity.Image1 = userActivityArray.getJSONObject(i).getString("Image1")
            userActivitymasterArray.add(userActivity)
        }
        userDetailInfo.Useractivities = userActivitymasterArray


        val profileInfoModel = Profile()
        val profile = jsonObject.getString("profile")
        val profileJsonObj = JSONObject(profile)

        profileInfoModel.userstatus = profileJsonObj.getString("userstatus")
        profileInfoModel.strength = profileJsonObj.getDouble("strength")
        profileInfoModel.statename = profileJsonObj.getString("statename")
        profileInfoModel.scode = profileJsonObj.getString("scode")
        profileInfoModel.rera = profileJsonObj.getString("rera")
        profileInfoModel.salemincost = profileJsonObj.getString("salemincost")
        profileInfoModel.salemaxcost = profileJsonObj.getString("salemaxcost")
        profileInfoModel.userid = profileJsonObj.getInt("userid")
        profileInfoModel.profilepic = profileJsonObj.getString("profilepic")
        profileInfoModel.password = profileJsonObj.getString("password")
        profileInfoModel.name = profileJsonObj.getString("name")
        profileInfoModel.mobile = profileJsonObj.getString("mobile")
        profileInfoModel.lon = profileJsonObj.getString("lon")
        profileInfoModel.lat = profileJsonObj.getString("lat")
        profileInfoModel.state = profileJsonObj.getInt("state")
        profileInfoModel.email = profileJsonObj.getString("email")
        profileInfoModel.description = profileJsonObj.getString("description")
        profileInfoModel.customertype = profileJsonObj.getString("customertype")
        profileInfoModel.customer_link = profileJsonObj.getString("customer_link")
        profileInfoModel.cityname = profileJsonObj.getString("cityname")
        profileInfoModel.agencyname = profileJsonObj.getString("agencyname")
        profileInfoModel.salecount = profileJsonObj.getInt("salecount")
        profileInfoModel.address = profileJsonObj.getString("address")
        profileInfoModel.badges = profileJsonObj.getString("badges")
        profileInfoModel.rentmincost = profileJsonObj.getString("rentmincost")
        profileInfoModel.rentmaxcost = profileJsonObj.getString("rentmaxcost")
        profileInfoModel.rentcount = profileJsonObj.getInt("rentcount")
        profileInfoModel.rating = profileJsonObj.getDouble("rating")
        profileInfoModel.country = profileJsonObj.getInt("country")
        profileInfoModel.Operatingsince = profileJsonObj.getInt("Operatingsince")
        profileInfoModel.addedbyid = profileJsonObj.getInt("addedbyid")
        profileInfoModel.city = profileJsonObj.getInt("city")

        userDetailInfo.profile = profileInfoModel


        return userDetailInfo
    }


    fun parseContactOwnerDetails(response: String): UserDetailDataModel {
        val ownerContactInfo = UserDetailDataModel()
        val jsonObject = JSONObject(response)

        val profileInfoModel = Profile()
        val profile = jsonObject.getString("profile")
        val profileJsonObj = JSONObject(profile)

        profileInfoModel.profilepic = profileJsonObj.getString("profilepic")
        profileInfoModel.mobile = profileJsonObj.getString("mobile")
        profileInfoModel.email = profileJsonObj.getString("email")
        profileInfoModel.agencyname = profileJsonObj.getString("agencyname")

        ownerContactInfo.profile = profileInfoModel

        return ownerContactInfo
    }

    fun parsePropertySearchResponse(response: String): SearchPropertyDataModel {
        val searchInfo = SearchPropertyDataModel()
        val jsonObject = JSONObject(response)


        val featurePropertiesArrayList = ArrayList<FeaturePropertiesDataModel>()
        val propertiesArray = jsonObject.getJSONArray("Properties")
        for (i in 0 until propertiesArray.length()) {
            val featureProperties = FeaturePropertiesDataModel()
            featureProperties.id = propertiesArray.getJSONObject(i).getInt("id")
            featureProperties.saletypename =
                propertiesArray.getJSONObject(i).getString("saletypename")
            featureProperties.propertyid = propertiesArray.getJSONObject(i).getString("propertyid")
            featureProperties.projectid = propertiesArray.getJSONObject(i).getString("projectid")
            featureProperties.title = propertiesArray.getJSONObject(i).getString("title")
            featureProperties.description =
                propertiesArray.getJSONObject(i).getString("description")
            featureProperties.bedroom = propertiesArray.getJSONObject(i).getString("bedroom")
            featureProperties.bathroom = propertiesArray.getJSONObject(i).getString("bathroom")
            featureProperties.totalarea = propertiesArray.getJSONObject(i).getString("totalarea")
            featureProperties.categoryname =
                propertiesArray.getJSONObject(i).getString("categoryname")
            featureProperties.subcategoryname =
                propertiesArray.getJSONObject(i).getString("subcategoryname")
            featureProperties.possessionname =
                propertiesArray.getJSONObject(i).getString("possessionname")
            featureProperties.cityname = propertiesArray.getJSONObject(i).getString("cityname")
            featureProperties.costinword = propertiesArray.getJSONObject(i).getString("costinword")
            featureProperties.image1 = propertiesArray.getJSONObject(i).getString("image1")
            featureProperties.isverified = propertiesArray.getJSONObject(i).getString("isverified")
            featureProperties.verfiediconpath =
                propertiesArray.getJSONObject(i).getString("verfiediconpath")
            featureProperties.link = propertiesArray.getJSONObject(i).getString("link")
            featureProperties.customertype =
                propertiesArray.getJSONObject(i).getString("customertype")
            featureProperties.addedbyid = propertiesArray.getJSONObject(i).getInt("addedbyid")
            featureProperties.name = propertiesArray.getJSONObject(i).getString("name")
            featureProperties.profilepic = propertiesArray.getJSONObject(i).getString("profilepic")
            featureProperties.badges = propertiesArray.getJSONObject(i).getString("badges")
            featureProperties.strength = propertiesArray.getJSONObject(i).getDouble("strength")
            featureProperties.strengthmsg =
                propertiesArray.getJSONObject(i).getString("strengthmsg")
            featureProperties.address = propertiesArray.getJSONObject(i).getString("address")
            featureProperties.rating = propertiesArray.getJSONObject(i).getDouble("rating")
            featureProperties.lat = propertiesArray.getJSONObject(i).getString("lat")
            featureProperties.lon = propertiesArray.getJSONObject(i).getString("lon")
            featureProperties.isfavourite =
                propertiesArray.getJSONObject(i).getString("isfavourite")
            featureProperties.link = propertiesArray.getJSONObject(i).getString("link")
            featurePropertiesArrayList.add(featureProperties)
        }
        searchInfo.Properties = featurePropertiesArrayList

        val advertismentmasterArray = ArrayList<Advertisment>()
        val advertiseArray = jsonObject.getJSONArray("advertisment")
        for (i in 0 until advertiseArray.length()) {
            val advertise = Advertisment()

            advertise.id = advertiseArray.getJSONObject(i).getInt("id")
            advertise.title = advertiseArray.getJSONObject(i).getString("title")
            advertise.statename = advertiseArray.getJSONObject(i).getString("statename")
            advertise.sdate = advertiseArray.getJSONObject(i).getString("sdate")
            advertise.property = advertiseArray.getJSONObject(i).getInt("property")
            advertise.project = advertiseArray.getJSONObject(i).getInt("project")
            advertise.placement = advertiseArray.getJSONObject(i).getString("placement")
            advertise.locality = advertiseArray.getJSONObject(i).getString("locality")
            advertise.link = advertiseArray.getJSONObject(i).getString("link")
            advertise.image = advertiseArray.getJSONObject(i).getString("image")
            advertise.edate = advertiseArray.getJSONObject(i).getString("edate")
            advertise.cityname = advertiseArray.getJSONObject(i).getString("cityname")
            advertise.badges = advertiseArray.getJSONObject(i).getString("badges")
            advertise.address = advertiseArray.getJSONObject(i).getString("address")
            advertise.addtype = advertiseArray.getJSONObject(i).getString("addtype")
            advertise.agency = advertiseArray.getJSONObject(i).getInt("agency")
            advertise.agent = advertiseArray.getJSONObject(i).getInt("agent")
            advertismentmasterArray.add(advertise)
        }
        searchInfo.advertisment = advertismentmasterArray

        searchInfo.searchid = jsonObject.getInt("searchid")
        searchInfo.searchdescription = jsonObject.getString("searchdescription")

        return searchInfo
    }

    fun parseProjectSearchResponse(response: String): SearchProjectDataModel {
        val searchInfo = SearchProjectDataModel()
        val jsonObject = JSONObject(response)

        val advertismentmasterArray = ArrayList<Advertisment>()
        val advertiseArray = jsonObject.getJSONArray("advertisment")
        for (i in 0 until advertiseArray.length()) {
            val advertise = Advertisment()

            advertise.id = advertiseArray.getJSONObject(i).getInt("id")
            advertise.title = advertiseArray.getJSONObject(i).getString("title")
            advertise.statename = advertiseArray.getJSONObject(i).getString("statename")
            advertise.sdate = advertiseArray.getJSONObject(i).getString("sdate")
            advertise.property = advertiseArray.getJSONObject(i).getInt("property")
            advertise.project = advertiseArray.getJSONObject(i).getInt("project")
            advertise.placement = advertiseArray.getJSONObject(i).getString("placement")
            advertise.locality = advertiseArray.getJSONObject(i).getString("locality")
            advertise.link = advertiseArray.getJSONObject(i).getString("link")
            advertise.image = advertiseArray.getJSONObject(i).getString("image")
            advertise.edate = advertiseArray.getJSONObject(i).getString("edate")
            advertise.cityname = advertiseArray.getJSONObject(i).getString("cityname")
            advertise.badges = advertiseArray.getJSONObject(i).getString("badges")
            advertise.address = advertiseArray.getJSONObject(i).getString("address")
            advertise.addtype = advertiseArray.getJSONObject(i).getString("addtype")
            advertise.agency = advertiseArray.getJSONObject(i).getInt("agency")
            advertise.agent = advertiseArray.getJSONObject(i).getInt("agent")
            advertismentmasterArray.add(advertise)
        }
        searchInfo.advertisment = advertismentmasterArray

        searchInfo.searchid = jsonObject.getInt("searchid")
        searchInfo.searchdescription = jsonObject.getString("searchdescription")


        val projectsArrayList = ArrayList<ProjectsDataModel>()
        val projectsArray = jsonObject.getJSONArray("projects")
        for (i in 0 until projectsArray.length()) {
            val projects = ProjectsDataModel()
            projects.id = projectsArray.getJSONObject(i).getInt("id")
            projects.projectname =
                projectsArray.getJSONObject(i).getString("projectname")
            projects.Fulladdress = projectsArray.getJSONObject(i).getString("Fulladdress")
            projects.projectid = projectsArray.getJSONObject(i).getString("projectid")
            projects.mincost = projectsArray.getJSONObject(i).getString("mincost")
            projects.description =
                projectsArray.getJSONObject(i).getString("ddescription")
            projects.maxcost = projectsArray.getJSONObject(i).getString("maxcost")
            projects.configuration = projectsArray.getJSONObject(i).getString("configuration")
            projects.configuration2 = projectsArray.getJSONObject(i).getString("configuration2")
            projects.categoryname =
                projectsArray.getJSONObject(i).getString("categoryname")
            projects.subcategoryname =
                projectsArray.getJSONObject(i).getString("subcategoryname")
            projects.possessionname =
                projectsArray.getJSONObject(i).getString("possessionname")
            projects.image1 = projectsArray.getJSONObject(i).getString("image1")
            projects.customertype =
                projectsArray.getJSONObject(i).getString("customertype")
            projects.addedbyid = projectsArray.getJSONObject(i).getInt("addedbyid")
            projects.name = projectsArray.getJSONObject(i).getString("name")
            projects.strength = projectsArray.getJSONObject(i).getDouble("strength")
            projects.strengthmsg = projectsArray.getJSONObject(i).getString("strengthmsg")
            projects.rating = projectsArray.getJSONObject(i).getDouble("rating")
            projects.lat = projectsArray.getJSONObject(i).getString("lat")
            projects.lon = projectsArray.getJSONObject(i).getString("lon")
            projects.isfavourite = projectsArray.getJSONObject(i).getString("isfavourite")
            projects.badges = projectsArray.getJSONObject(i).getString("badges")
            projects.linkurl = projectsArray.getJSONObject(i).getString("linkurl")
            projects.profilepic = projectsArray.getJSONObject(i).getString("profilepic")

            projectsArrayList.add(projects)
        }
        searchInfo.projects = projectsArrayList

        return searchInfo
    }

    fun parseAuctionSearchResponse(response: String): SearchAuctionDataModel {
        val searchInfo = SearchAuctionDataModel()
        val jsonObject = JSONObject(response)

        val advertismentmasterArray = ArrayList<Advertisment>()
        val advertiseArray = jsonObject.getJSONArray("advertisment")
        for (i in 0 until advertiseArray.length()) {
            val advertise = Advertisment()

            advertise.id = advertiseArray.getJSONObject(i).getInt("id")
            advertise.title = advertiseArray.getJSONObject(i).getString("title")
            advertise.statename = advertiseArray.getJSONObject(i).getString("statename")
            advertise.sdate = advertiseArray.getJSONObject(i).getString("sdate")
            advertise.property = advertiseArray.getJSONObject(i).getInt("property")
            advertise.project = advertiseArray.getJSONObject(i).getInt("project")
            advertise.placement = advertiseArray.getJSONObject(i).getString("placement")
            advertise.locality = advertiseArray.getJSONObject(i).getString("locality")
            advertise.link = advertiseArray.getJSONObject(i).getString("link")
            advertise.image = advertiseArray.getJSONObject(i).getString("image")
            advertise.edate = advertiseArray.getJSONObject(i).getString("edate")
            advertise.cityname = advertiseArray.getJSONObject(i).getString("cityname")
            advertise.badges = advertiseArray.getJSONObject(i).getString("badges")
            advertise.address = advertiseArray.getJSONObject(i).getString("address")
            advertise.addtype = advertiseArray.getJSONObject(i).getString("addtype")
            advertise.agency = advertiseArray.getJSONObject(i).getInt("agency")
            advertise.agent = advertiseArray.getJSONObject(i).getInt("agent")
        }

        searchInfo.advertisment = advertismentmasterArray

        searchInfo.searchid = jsonObject.getInt("searchid")
        searchInfo.searchdescription = jsonObject.getString("searchdescription")


        val auctionmasterArray = ArrayList<AuctionSearchInfoModel>()
        val auctionArray = jsonObject.getJSONArray("Auction")
        for (i in 0 until auctionArray.length()) {
            val auction = AuctionSearchInfoModel()

            auction.id = auctionArray.getJSONObject(i).getInt("id")
            auction.title = auctionArray.getJSONObject(i).getString("title")
            auction.website = auctionArray.getJSONObject(i).getString("website")
            auction.subcategoryname = auctionArray.getJSONObject(i).getString("subcategoryname")
            auction.stime = auctionArray.getJSONObject(i).getString("stime")
            auction.status = auctionArray.getJSONObject(i).getString("status")
            auction.startdate = auctionArray.getJSONObject(i).getString("startdate")
            auction.reservepriceinword =
                auctionArray.getJSONObject(i).getString("reservepriceinword")
            auction.researveprice = auctionArray.getJSONObject(i).getString("researveprice")
            auction.possessiontypename =
                auctionArray.getJSONObject(i).getString("possessiontypename")
            auction.locatiodesc = auctionArray.getJSONObject(i).getString("locatiodesc")
            auction.locality = auctionArray.getJSONObject(i).getString("locality")
            auction.linkurl = auctionArray.getJSONObject(i).getString("linkurl")
            auction.inspectiondate = auctionArray.getJSONObject(i).getString("inspectiondate")
            auction.image1 = auctionArray.getJSONObject(i).getString("image1")
            auction.etime = auctionArray.getJSONObject(i).getString("etime")
            auction.emdsubmission = auctionArray.getJSONObject(i).getString("emdsubmission")
            auction.emdpaymentmethod = auctionArray.getJSONObject(i).getString("emdpaymentmethod")
            auction.emdamountinword = auctionArray.getJSONObject(i).getString("emdamountinword")
            auction.emdamount = auctionArray.getJSONObject(i).getString("emdamount")
            auction.createddate = auctionArray.getJSONObject(i).getString("createddate")
            auction.contactdetails = auctionArray.getJSONObject(i).getString("contactdetails")
            auction.cityname = auctionArray.getJSONObject(i).getString("cityname")
            auction.address = auctionArray.getJSONObject(i).getString("address")
            auction.area = auctionArray.getJSONObject(i).getString("area")
            auction.areaunit = auctionArray.getJSONObject(i).getString("areaunit")
            auction.auctionfiles = auctionArray.getJSONObject(i).getString("auctionfiles")
            auction.auctionprovider = auctionArray.getJSONObject(i).getString("auctionprovider")
            auction.auid = auctionArray.getJSONObject(i).getString("auid")
            auction.bankname = auctionArray.getJSONObject(i).getString("bankname")
            auction.borrowername = auctionArray.getJSONObject(i).getString("borrowername")
            auction.cityname = auctionArray.getJSONObject(i).getString("cityname")
            auction.lat = auctionArray.getJSONObject(i).getDouble("lat")
            auction.lon = auctionArray.getJSONObject(i).getDouble("lon")
            auctionmasterArray.add(auction)
        }

        searchInfo.Auction = auctionmasterArray

        return searchInfo
    }

    fun parseAgencySearchResponse(response: String): SearchAgencyDataModel {
        val searchInfo = SearchAgencyDataModel()
        val jsonObject = JSONObject(response)

        val advertismentmasterArray = ArrayList<Advertisment>()
        val advertiseArray = jsonObject.getJSONArray("advertisment")
        for (i in 0 until advertiseArray.length()) {
            val advertise = Advertisment()

            advertise.id = advertiseArray.getJSONObject(i).getInt("id")
            advertise.title = advertiseArray.getJSONObject(i).getString("title")
            advertise.statename = advertiseArray.getJSONObject(i).getString("statename")
            advertise.sdate = advertiseArray.getJSONObject(i).getString("sdate")
            advertise.property = advertiseArray.getJSONObject(i).getInt("property")
            advertise.project = advertiseArray.getJSONObject(i).getInt("project")
            advertise.placement = advertiseArray.getJSONObject(i).getString("placement")
            advertise.locality = advertiseArray.getJSONObject(i).getString("locality")
            advertise.link = advertiseArray.getJSONObject(i).getString("link")
            advertise.image = advertiseArray.getJSONObject(i).getString("image")
            advertise.edate = advertiseArray.getJSONObject(i).getString("edate")
            advertise.cityname = advertiseArray.getJSONObject(i).getString("cityname")
            advertise.badges = advertiseArray.getJSONObject(i).getString("badges")
            advertise.address = advertiseArray.getJSONObject(i).getString("address")
            advertise.addtype = advertiseArray.getJSONObject(i).getString("addtype")
            advertise.agency = advertiseArray.getJSONObject(i).getInt("agency")
            advertise.agent = advertiseArray.getJSONObject(i).getInt("agent")
            advertismentmasterArray.add(advertise)
        }
        searchInfo.advertisment = advertismentmasterArray

        searchInfo.searchid = jsonObject.getInt("searchid")
        searchInfo.searchdescription = jsonObject.getString("searchdescription")


        val profilemasterArray = ArrayList<Profile>()
        val profileArray = jsonObject.getJSONArray("userProfiles")
        for (i in 0 until profileArray.length()) {
            val profile = Profile()
            val profileJsonObj = profileArray.getJSONObject(i)
            profile.userstatus = profileJsonObj.getString("userstatus")
            profile.strength = profileJsonObj.getDouble("strength")
            profile.statename = profileJsonObj.getString("statename")
            profile.scode = profileJsonObj.getString("scode")
            profile.rera = profileJsonObj.getString("rera")
            profile.salemincost = profileJsonObj.getString("salemincost")
            profile.salemaxcost = profileJsonObj.getString("salemaxcost")
            profile.userid = profileJsonObj.getInt("userid")
            profile.profilepic = profileJsonObj.getString("profilepic")
            profile.password = profileJsonObj.getString("password")
            profile.name = profileJsonObj.getString("name")
            profile.mobile = profileJsonObj.getString("mobile")
            profile.lon = profileJsonObj.getString("lon")
            profile.lat = profileJsonObj.getString("lat")
            profile.state = profileJsonObj.getInt("state")
            profile.email = profileJsonObj.getString("email")
            profile.description = profileJsonObj.getString("description")
            profile.customertype = profileJsonObj.getString("customertype")
            profile.customer_link = profileJsonObj.getString("customer_link")
            profile.cityname = profileJsonObj.getString("cityname")
            profile.agencyname = profileJsonObj.getString("agencyname")
            profile.salecount = profileJsonObj.getInt("salecount")
            profile.address = profileJsonObj.getString("address")
            profile.badges = profileJsonObj.getString("badges")
            profile.rentmincost = profileJsonObj.getString("rentmincost")
            profile.rentmaxcost = profileJsonObj.getString("rentmaxcost")
            profile.rentcount = profileJsonObj.getInt("rentcount")
            profile.rating = profileJsonObj.getDouble("rating")
            profile.country = profileJsonObj.getInt("country")
            profile.Operatingsince = profileJsonObj.getInt("Operatingsince")
            profile.addedbyid = profileJsonObj.getInt("addedbyid")
            profile.city = profileJsonObj.getInt("city")
            profilemasterArray.add(profile)
        }
        searchInfo.userProfiles = profilemasterArray

        return searchInfo
    }

    fun parseAuctionDetailResponse(response: String): AuctionDetailDataModel {
        val auctionInfo = AuctionDetailDataModel()
        val jsonObject = JSONObject(response)

        val auctionmasterArray = ArrayList<Auction>()
        val auctionArray = jsonObject.getJSONArray("Auction")
        for (i in 0 until auctionArray.length()) {
            val auctions = Auction()
            auctions.id = auctionArray.getJSONObject(i).getInt("id")
            auctions.website = auctionArray.getJSONObject(i).getString("website")
            auctions.title = auctionArray.getJSONObject(i).getString("title")
            auctions.subcategoryname = auctionArray.getJSONObject(i).getString("subcategoryname")
            auctions.stime = auctionArray.getJSONObject(i).getString("stime")
            auctions.status = auctionArray.getJSONObject(i).getString("status")
            auctions.startdate = auctionArray.getJSONObject(i).getString("startdate")
            auctions.reservepriceinword =
                auctionArray.getJSONObject(i).getString("reservepriceinword")
            auctions.researveprice = auctionArray.getJSONObject(i).getString("researveprice")
            auctions.possessiontypename =
                auctionArray.getJSONObject(i).getString("possessiontypename")
            auctions.locatiodesc = auctionArray.getJSONObject(i).getString("locatiodesc")
            auctions.locality = auctionArray.getJSONObject(i).getString("locality")
            auctions.linkurl = auctionArray.getJSONObject(i).getString("linkurl")
            auctions.inspectiondate = auctionArray.getJSONObject(i).getString("inspectiondate")
            auctions.image1 = auctionArray.getJSONObject(i).getString("image1")
            auctions.etime = auctionArray.getJSONObject(i).getString("etime")
            auctions.enddate = auctionArray.getJSONObject(i).getString("enddate")
            auctions.emdsubmission = auctionArray.getJSONObject(i).getString("emdsubmission")
            auctions.emdpaymentmethod = auctionArray.getJSONObject(i).getString("emdpaymentmethod")
            auctions.emdamountinword = auctionArray.getJSONObject(i).getString("emdamountinword")
            auctions.emdamount = auctionArray.getJSONObject(i).getString("emdamount")
            auctions.createddate = auctionArray.getJSONObject(i).getString("createddate")
            auctions.contactdetails = auctionArray.getJSONObject(i).getString("contactdetails")
            auctions.cityname = auctionArray.getJSONObject(i).getString("cityname")
            auctions.act = auctionArray.getJSONObject(i).getString("act")
            auctions.address = auctionArray.getJSONObject(i).getString("address")
            auctions.area = auctionArray.getJSONObject(i).getString("area")
            auctions.areaunit = auctionArray.getJSONObject(i).getString("areaunit")
            auctions.auctionfiles = auctionArray.getJSONObject(i).getString("auctionfiles")
            auctions.auctionprovider = auctionArray.getJSONObject(i).getString("auctionprovider")
            auctions.auid = auctionArray.getJSONObject(i).getString("auid")
            auctions.bankname = auctionArray.getJSONObject(i).getString("bankname")
            auctions.borrowername = auctionArray.getJSONObject(i).getString("borrowername")
            auctions.categoryname = auctionArray.getJSONObject(i).getString("categoryname")
            auctions.lat = auctionArray.getJSONObject(i).getDouble("lat")
            auctions.lon = auctionArray.getJSONObject(i).getDouble("lon")
            auctions.linkurl = auctionArray.getJSONObject(i).getString("linkurl")

            auctionmasterArray.add(auctions)
        }
        auctionInfo.Auction = auctionmasterArray


        val advertismentmasterArray = ArrayList<Advertisment>()
        val advertiseArray = jsonObject.getJSONArray("advertisment")
        for (i in 0 until advertiseArray.length()) {
            val advertise = Advertisment()

            advertise.id = advertiseArray.getJSONObject(i).getInt("id")
            advertise.title = advertiseArray.getJSONObject(i).getString("title")
            advertise.statename = advertiseArray.getJSONObject(i).getString("statename")
            advertise.sdate = advertiseArray.getJSONObject(i).getString("sdate")
            advertise.property = advertiseArray.getJSONObject(i).getInt("property")
            advertise.project = advertiseArray.getJSONObject(i).getInt("project")
            advertise.placement = advertiseArray.getJSONObject(i).getString("placement")
            advertise.locality = advertiseArray.getJSONObject(i).getString("locality")
            advertise.link = advertiseArray.getJSONObject(i).getString("link")
            advertise.image = advertiseArray.getJSONObject(i).getString("image")
            advertise.edate = advertiseArray.getJSONObject(i).getString("edate")
            advertise.cityname = advertiseArray.getJSONObject(i).getString("cityname")
            advertise.badges = advertiseArray.getJSONObject(i).getString("badges")
            advertise.address = advertiseArray.getJSONObject(i).getString("address")
            advertise.addtype = advertiseArray.getJSONObject(i).getString("addtype")
            advertise.agency = advertiseArray.getJSONObject(i).getInt("agency")
            advertise.agent = advertiseArray.getJSONObject(i).getInt("agent")
            advertismentmasterArray.add(advertise)
        }
        auctionInfo.advertisment = advertismentmasterArray

        val auctiondocumentmasterArray = ArrayList<Auctiondocument>()
        val auctiondocumentArray = jsonObject.getJSONArray("auctiondocuments")
        for (i in 0 until auctiondocumentArray.length()) {
            val auctiondocument = Auctiondocument()
            auctiondocument.id = auctiondocumentArray.getJSONObject(i).getInt("id")
            auctiondocument.docpath = auctiondocumentArray.getJSONObject(i).getString("docpath")
            auctiondocument.docname = auctiondocumentArray.getJSONObject(i).getString("docname")
            auctiondocument.auid = auctiondocumentArray.getJSONObject(i).getInt("auid")
            auctiondocumentmasterArray.add(auctiondocument)
        }
        auctionInfo.auctiondocuments = auctiondocumentmasterArray


        return auctionInfo
    }

    fun parseChatsResponse(response: String): ChatsDataModel {
        val chatInfo = ChatsDataModel()
        val jsonObject = JSONObject(response)

        chatInfo.agencyname = jsonObject.getString("agencyname")
        chatInfo.name = jsonObject.getString("name")
        chatInfo.logo = jsonObject.getString("logo")

        val chatmasterArray = ArrayList<Chat>()
        val chatArray = jsonObject.getJSONArray("chat")
        for (i in 0 until chatArray.length()) {
            val chat = Chat()

            chat.id = chatArray.getJSONObject(i).getInt("id")
            chat.comment = chatArray.getJSONObject(i).getString("comment")
            chat.postedsince = chatArray.getJSONObject(i).getString("postedsince")

            chatmasterArray.add(chat)
        }
        chatInfo.chat = chatmasterArray

        return chatInfo
    }


    fun parseBlogsList(response: String): ArrayList<BlogDataModel> {
        val blogsList = ArrayList<BlogDataModel>()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val blogs = BlogDataModel()
            blogs.id = jsonArray.getJSONObject(i).getInt("id")
            blogs.image = jsonArray.getJSONObject(i).getString("image")
            blogs.heading = jsonArray.getJSONObject(i).getString("heading")
            blogs.link = jsonArray.getJSONObject(i).getString("link")
            blogs.bdate = jsonArray.getJSONObject(i).getString("bdate")
            blogs.author = jsonArray.getJSONObject(i).getString("author")
            blogs.longdescription = jsonArray.getJSONObject(i).getString("longdescription")
            blogs.status = jsonArray.getJSONObject(i).getString("status")
            blogsList.add(blogs)
        }
        return blogsList
    }
}