package com.alisafdar.enocassessment.resources.persistence

import com.alisafdar.enocassessment.data.persistance.UserDao
import com.alisafdar.enocassessment.data.responses.User
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserDaoTest : LocalDatabase() {

  private lateinit var userDao: UserDao

  @Before
  fun init() {
    userDao = db.userDao()
  }

  @Test
  fun `insert user and get user`() = runBlocking {
    val user = User(1, "Ali", "ali@gmail.com")
    userDao.insertUser(user)

    val dbUser = userDao.getUser()
    Assert.assertTrue(user.id == dbUser.id)
    Assert.assertTrue(user.name == dbUser.name)
    Assert.assertTrue(user.email == dbUser.email)
  }
}
