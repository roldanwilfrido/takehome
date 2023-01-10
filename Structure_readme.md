### Responses structure

- OK filter response:

```json
    {
      "continent": [
        {
          "countries": [...],
          "name": "...",
          "otherCountries": [...]
        }
      ],[...]
    }
```

- Exception responses:

    * Bad/missing 'countryCodes' parameter:

    ```json
    {
        "time": "timestamp",
        "status": 400,
        "message": "Required request parameter 'countryCodes' for method parameter type Set is not present"
    }
    ```  

    * Empty values in 'countryCodes' parameter:

    ```json
    {
        "time": "timestamp",
        "status": 400,
        "message": "'countryCodes' parameter must have 1 value at least, please check."
    }
    ```  

    * Too much requests:

    ```json
    {
        "time": "timestamp",
        "status": 429,
        "message": "Too many requests, please try later."
    }
    ```  

    * Ops! Changes on [Countries Trevor Blades API](https://countries.trevorblades.com/graphql)?:

    ```json
    {
        "time": "timestamp",
        "status": 422,
        "message": "..."
    }
    ```  

    * Cannot establish connection with [Countries Trevor Blades API](https://countries.trevorblades.com/graphql):

    ```json
    {
        "time": "timestamp",
        "status": 500,
        "message": "..."
    }
    ```