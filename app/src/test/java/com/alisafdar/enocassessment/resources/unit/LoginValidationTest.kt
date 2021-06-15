package com.alisafdar.enocassessment.resources.unit

import com.alisafdar.enocassessment.utils.LoginValidator
import org.junit.Assert
import org.junit.Test

class LoginValidationTest {
    @Test
    fun `login if email is blank then fail` () {
        val username = ""
        Assert.assertFalse(LoginValidator.isEmailValid(username))
    }

    @Test
    fun `login if email is valid then pass`() {
        val username = "abc@example.com"
        Assert.assertTrue(LoginValidator.isEmailValid(username))
    }

    @Test
    fun `login if password is short then fail`() {
        val password = "abc"
        Assert.assertFalse(LoginValidator.isPasswordValid(password))
    }

    @Test
    fun `login if password is valid then pass`() {
        val password = "abc123"
        Assert.assertTrue(LoginValidator.isPasswordValid(password))
    }
}