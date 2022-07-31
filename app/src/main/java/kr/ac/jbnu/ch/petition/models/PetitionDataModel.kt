package kr.ac.jbnu.ch.petition.models

data class PetitionDataModel(val category : String, val id : String, val author : String, val contents : String, val imageIndex : Int, val recommend : Int, val timeStamp : String, val title : String, val status : PetitionStatusModel) {
    override fun equals(other: Any?): Boolean {
        var retVal = false

        if(other is PetitionDataModel){
            val ptr : PetitionDataModel = other as PetitionDataModel
            retVal = ptr.id == this.id
        }

        return retVal
    }
}