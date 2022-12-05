import org.springframework.cloud.contract.spec.Contract


Contract.make {
    description "Should return new Courier wit id and user_id when valid data provided"
    request {
        method POST()
        headers {
            contentType(applicationJson())
        }
        url("/register") {
            body(
                    firstName : "Anna",
                    lastName: "Panna",
                    email: "used@mail.com"
            )
        }
    }
    response {
        status 400
    }
}