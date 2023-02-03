# Tour de App - Spring Boot boiler plate

Šablona pro vývoj aplikace pro Tour de App společně s vytvořením a nahráním výstupu

## Lokální spuštění

### Docker
#### Prerekvizity
- Docker
- (Windows) aktivovaný wsl2

#### Spuštění
```
docker build . -t tda-springboot
docker run -p 8080:80 -v ${PWD}:/app tda-springboot
```

Aplikace bude přístupná na `http://127.0.0.1:8080`

## Odevzdání
V rámci GitHub akce se aplikace automaticky odevzdává, jediné co je potřeba udělat je v rámci repozitáře si nastavit svůj vlastní TEAM\_SECRET, který dostanete po registraci do soutěže


### Databáze
V kontejneru je zahrnuta H2 embedovaná databáze konfigurovaná k ukládání do souboru uvnitř kontejneru.
Tato konfigurace je specifikována v souboru applications.properties

Přístupové údaje k databázi jsou:
user=sa
password=password

Ke databázové konzoli se můžete připojit na
http://127.0.0.1:8080/h2-console
