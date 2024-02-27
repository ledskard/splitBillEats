O frontend está bem simples em HTML e JS puro, está na pasta frontend na raiz do projeto.

- Curl da requisição

curl --location 'localhost:8080/orders' \
--header 'Content-Type: application/json' \
--data '{
    "discount": 20,
    "deliveryFee": 8,
    "discountPercentage": 10,
    "serviceChargePercentage": 10,
    "users": [
        {
            "name": "Amigo 1",
            "items": [
                {
                    "quantity": 1,
                    "name": "sanduiche",
                    "price": 8
                }
            ]
        },
        {
            "name": "Amigo 2",
            "items": [
                {
                    "quantity": 1,
                    "name": "hamburguer",
                    "price": 40
                },
                {
                    "quantity": 1,
                    "name": "sobremesa",
                    "price": 2
                }
            ]
        }
    ]
}'
