package com.superkonduktr.challenge

sealed trait Error

object Error {
  case object FileDoesNotExist extends Error
  case object FileAlreadyExists extends Error
  case object UnsupportedFormat extends Error
}
