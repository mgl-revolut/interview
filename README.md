Project Overview
================
Framework used: [Spark Java](http://sparkjava.com)

## API

The following endpoints have been implemented

__Create new customer__
- **Url:** /customer
- **Method:** POST
- **Body:** 

```javascript
{
    "firstName": "myFirstName",
    "lastName": "myLastName",
    "email": "myEmail"
}
```

__Create new account__
- **Url:** /account
- **Method:** POST
- **Body:** 

```javascript
{
    "customerId" : "1de01b49-8578-4f0e-8462-2ad14af707ee"
}
```

__Get account__
- **Url:** /account/{accountId}
- **Method:** GET
- **Body:** --

__Create new wallet__
- **Url:** /wallet
- **Method:** POST
- **Body:** 

```javascript
{
    "currency": "GBP"
}
```


__Make deposit to wallet__
- **Url:** /wallet/{walletId}/deposit
- **Method:** POST
- **Body:** 

```javascript
{
    "amount" : "100.12"
}
```


__Transfer money between wallets__
- **Url:** /transferMoney
- **Method:** POST
- **Body:** 

```javascript
{
    "senderWalletId" : "47692ca2-ef89-4326-9ebd-38c3b9c5e539",
    "beneficiaryWalletId": "deeac79a-ac3c-44e9-a974-698d7728ca9e",
    "amount": "10"
}
```



