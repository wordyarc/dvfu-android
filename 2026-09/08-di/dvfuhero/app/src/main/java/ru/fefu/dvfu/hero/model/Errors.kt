package ru.fefu.dvfu.hero.model

class ItemNotFoundException : Exception()

class ItemLoadException(val canRetry: Boolean, cause: Throwable) : Exception(cause)
