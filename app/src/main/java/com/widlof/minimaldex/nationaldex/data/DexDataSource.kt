package com.widlof.minimaldex.nationaldex.data

import com.widlof.minimaldex.nationaldex.data.model.NationalDexResponse
import com.widlof.minimaldex.network.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface DexDataSource {
    suspend fun getNationalDex(): NetworkResponse<NationalDexResponse?>
}