package com.sample.ecommerce.data.remote.model

/**
 * Sample JSON responses matching the API contract for reference and manual testing.
 * Use these when the API is unavailable or for unit tests.
 */
object SampleAuthResponses {

    /** Sample success response for login/signUp - parse with Gson to get [ApiResponse]<[AuthResponseData]>. */
    const val SUCCESS_LOGIN = """
    {
        "success": true,
        "code": 200,
        "message": "Login successful",
        "data": {
            "user_id": "12345",
            "name": "John Doe",
            "email": "john@example.com",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
        }
    }
    """

    /** Sample success response for sign up / register. */
    const val SUCCESS_SIGN_UP = """
    {
        "success": true,
        "code": 201,
        "message": "Registration successful",
        "data": {
            "user_id": "67890",
            "name": "Jane Smith",
            "email": "jane@example.com",
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.sample"
        }
    }
    """

    /** Sample error response - invalid credentials. */
    const val ERROR_INVALID_CREDENTIALS = """
    {
        "success": false,
        "code": 401,
        "message": "Invalid email or password"
    }
    """

    /** Sample error response - email already registered. */
    const val ERROR_EMAIL_EXISTS = """
    {
        "success": false,
        "code": 409,
        "message": "Email already registered"
    }
    """

    /** Sample error response - validation. */
    const val ERROR_VALIDATION = """
    {
        "success": false,
        "code": 400,
        "message": "Invalid request. Please check your input."
    }
    """
}
