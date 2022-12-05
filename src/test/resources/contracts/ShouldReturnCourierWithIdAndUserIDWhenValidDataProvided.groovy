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
                    firstName : "Magdalena",
                    lastName: "Wawrzak",
                    email: "mw@mail.com",
            )
        }
    }
    response {
        body(
                id: 2,
                firstName : "Magdalena",
                lastName: "Wawrzak",
                email: "mw@mail.com",
                userId: 888
        )
        status 200
    }
}