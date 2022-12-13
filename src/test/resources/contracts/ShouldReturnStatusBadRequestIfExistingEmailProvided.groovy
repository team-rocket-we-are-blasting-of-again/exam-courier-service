import org.springframework.cloud.contract.spec.Contract


Contract.make {
    description "Should return 400 "
    request {
        method POST()
        headers {
            contentType(applicationJson())
        }
        url("/register") {
            body(
                    firstName : "Anna",
                    lastName: "Panna",
                    email: "used@mail.com",
                    password: "password"
            )
        }
    }
    response {
        status 400
    }
}