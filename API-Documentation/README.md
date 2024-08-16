# API Documentation Guide

## Integration Instructions

### API Gateway Usage

To integrate and use these APIs, do **not** use the individual microservice URLs and ports. Instead, you should direct all API requests to the API Gateway:

http://localhost:8080/

The API Gateway handles routing to the appropriate microservice, ensuring a unified and simplified interaction point.

Example: http://localhost:8080/api/v1/auth/

### Authorization

All API requests must include a JWT Token (access token) in the headers. This token should be set in the `Authorization` header as follows:

Authorization: Bearer <your_jwt_token>

### Obtaining the JWT Token

To obtain the JWT Token, you need to authenticate using the login API. Here is a sample request to the login API:

**Request:**

POST /api/v1/auth/authenticate

Host: localhost:8080

Content-Type: application/json

{
"email": "user@gmail.com",
"password": "00000000"
}


**Response:**

Upon successful authentication, you will receive a response containing the access token:

{
"access_token": "your_jwt_token",
...
}

Use this `accessToken` in the `Authorization` header for subsequent API requests.

### Example API Request

Here's an example of how to make an authorized API request:

GET /api/some-endpoint

Host: localhost:8080

Authorization: Bearer `<your_jwt_token>`


Ensure that you replace `your_jwt_token` with the actual token received from the login API.

## Important Notes

- Always use the API Gateway URL (`http://localhost:8080/`) for all API requests.
- Ensure the `Authorization` header is set with the JWT Token for every API request to access protected resources.

By following these guidelines, you will be able to effectively integrate and use the APIs provided in this documentation.
Feel free to adjust the content to fit any additional details or formatting preferences you might ha