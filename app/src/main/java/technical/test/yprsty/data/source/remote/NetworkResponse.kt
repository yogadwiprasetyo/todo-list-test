package technical.test.yprsty.data.source.remote

import com.google.gson.annotations.SerializedName

data class NetworkResponse(

	@field:SerializedName("country_code")
	val countryCode: String,

	@field:SerializedName("country")
	val country: String,

	@field:SerializedName("region")
	val region: String
)
