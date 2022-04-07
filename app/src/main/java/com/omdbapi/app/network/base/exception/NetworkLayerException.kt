package com.omdbapi.app.network.base.exception

import com.omdbapi.app.network.base.model.Error

class NetworkLayerException(message: String?, val errorModel: Error?) : Exception(message)