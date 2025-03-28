# h2-springboot-cache

### Swagger:

    http://localhost:3000/swagger-ui/index.html

```shell
curl -X POST http://localhost:3000/cache -H "Content-Type: application/json" -d '{"key": "username", "value": "h2-java"}'
```

```shell
curl -X GET http://localhost:3000/cache/username
```
