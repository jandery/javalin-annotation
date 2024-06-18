package se.refur.javalin

/**
 * The purpose of this exception is to handle errors in parsing annotated parameters
 */
class AnnotationParserException(message: String) : Exception(message)