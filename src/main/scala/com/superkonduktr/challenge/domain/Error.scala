package com.superkonduktr.challenge.domain

sealed trait Error

object Error {
  case object FileDoesNotExist extends Error
  case object FileAlreadyExists extends Error
  case object UnsupportedFormat extends Error
}
