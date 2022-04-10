Feature: Auth
  Background:
    * url 'http://localhost:8080/auth/'
    * def email = (this.__arg == null) ? "user@email.com" : this.__arg.email
    * def password = (this.__arg == null) ? "password" : this.__arg.password
    * def username = (this.__arg == null) ? "username" : this.__arg.username
    * def tokenParser =
      """
      function(token) {
          var base64Url = token.split('.')[1];
          var base64Str = base64Url.replace(/-/g, '+').replace(/_/g, '/');
          var Base64 = Java.type('java.util.Base64');
          var decoded = Base64.getDecoder().decode(base64Str);
          var String = Java.type('java.lang.String');
          return new String(decoded);
      }
      """

  Scenario: WHEN signing up with invalid email THEN return 400 and must be a well-formed email address error message
    * def email = 'invalid'
    Given path 'signup'
    Given request {"username":"#(username)","password":"#(password)","email":"#(email)"}
    And method POST
    Then status 400
    And match response.status == 'BAD_REQUEST'
    And match response.body == {"email":"must be a well-formed email address"}

  Scenario: WHEN signing up with invalid password THEN return 400 and size must be between 8 and 512 error message
    * def password = 'pwd'
    Given path 'signup'
    Given request {"username":"#(username)","password":"#(password)","email":"#(email)"}
    And method POST
    Then status 400
    And match response.status == 'BAD_REQUEST'
    And match response.body == {"password":"size must be between 8 and 512"}

  Scenario: WHEN signing up with invalid username THEN return 400 and size must be between 4 and 16 error message
    * def username = 'u/n'
    Given path 'signup'
    Given request {"username":"#(username)","password":"#(password)","email":"#(email)"}
    And method POST
    Then status 400
    And match response.status == 'BAD_REQUEST'
    And match response.body == {"username":"size must be between 4 and 16"}

  Scenario: WHEN signing up with invalid fields THEN return 400 and a map of three error messages
    * def email = 'invalid'
    * def password = 'pwd'
    * def username = 'u/n'
    Given path 'signup'
    Given request {"username":"#(username)","password":"#(password)","email":"#(email)"}
    And method POST
    Then status 400
    And match response.status == 'BAD_REQUEST'
    And match karate.sizeOf(response.body) == 3

  Scenario: WHEN signing up with already taken username THEN return 409 and is already taken error message
    Given path 'signup'
    Given request {"username":"#(username)","password":"#(password)","email":"#(email)"}
    And method POST
    Then status 201

    * def email = 'other@email.com'
    Given path 'signup'
    Given request {"username":"#(username)","password":"#(password)","email":"#(email)"}
    And method POST
    Then status 409
    And match response.status == 'CONFLICT'
    And match response.body == {"username":"username is already taken"}

  Scenario: WHEN signing up with already taken email THEN return 409 and is already taken error message
    * def username = 'other'
    Given path 'signup'
    Given request {"username":"#(username)","password":"#(password)","email":"#(email)"}
    And method POST
    Then status 409
    And match response.status == 'CONFLICT'
    And match response.body == {"email":"user@email.com is already taken"}

  Scenario: WHEN signing in with invalid credentials THEN return 401 and has invalid credentials error message
    * def password = 'invalid'
    Given path 'login'
    Given request {"username":"#(username)","password":"#(password)"}
    And method POST
    Then status 401
    And match response.body == {"user":"has invalid credentials"}}

  Scenario: WHEN signing in with valid credentials THEN return 200 and a access token
    Given path 'login'
    Given request {"username":"#(username)","password":"#(password)"}
    And method POST
    Then status 200
    And json token = tokenParser(response.token)
    And match token contains {"roles":"USER","username":"username"}


