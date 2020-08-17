package com.widlof.minimaldex.nationaldex.data

import com.widlof.minimaldex.nationaldex.data.DexDataSource
import com.widlof.minimaldex.nationaldex.data.model.NationalDexResponse
import com.widlof.minimaldex.network.NetworkResponse

class DexRepository(private val dataSource: DexDataSource): DexDataSource {
    private var nationalDexResponse: NationalDexResponse? = null

    override suspend fun getNationalDex(): NetworkResponse<NationalDexResponse?> {
        nationalDexResponse?.let {
            return NetworkResponse.Success(it)
        }
        val response = dataSource.getNationalDex()
        if (response is NetworkResponse.Success && response.responseBody != null) {
            nationalDexResponse = response.responseBody
        }
        return response
    }
}