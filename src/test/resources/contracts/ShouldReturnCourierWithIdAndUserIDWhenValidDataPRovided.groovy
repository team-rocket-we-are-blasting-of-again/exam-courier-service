import org.springframework.cloud.contract.spec.Contract


Contract.make {
    description "Should return new Courier wit id and user_id when valid data provided"
    request {
        method POST()
        url("/register") {
            body(
                    first_name : "Magdalena",
                    last_name: "Wawrzak",
                    email: "mw@mail.com"
            )
        }
    }
    response {
        body(
                id: 1,
                first_name : "Magdalena",
                last_name: "Wawrzak",
                email: "mw@mail.com",
                user_id: 888
        )
        status 200
    }
}