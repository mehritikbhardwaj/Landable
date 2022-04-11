package com.landable.app.ui.home.dataModels

import java.io.Serializable

class PropertyDetailsModel : Serializable {
    var Amenitiesmasters= ArrayList<Amenitiesmaster>()
    var additionaldetails= Additionaldetails()
    var advertisment= ArrayList<Advertisment>()
    var details= FeaturePropertiesDataModel()
    var distancefromlocation= ArrayList<Distancefromlocation>()
    var featuredproperty= ArrayList<FeaturePropertiesDataModel>()
    var propertyimages= ArrayList<Propertyimage>()

    var review= ArrayList<Review>()
    var similarproperty= ArrayList<FeaturePropertiesDataModel>()
}