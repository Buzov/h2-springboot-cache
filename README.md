# h2-springboot-cache

### Swagger:

```http://localhost:3000/swagger-ui/index.html```

### H2 Console:

```http://localhost:3000/h2-console```

### Insert a cache entry
```shell
curl -X POST http://localhost:3000/cache -H "Content-Type: application/json" -d '{"key": "username", "value": "h2-java"}'
```

### Get a cache info
```shell
curl -X GET http://localhost:3000/cache/username
```
