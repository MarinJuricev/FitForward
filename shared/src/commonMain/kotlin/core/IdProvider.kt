package core

fun interface IdProvider {
    fun generate(): String
}