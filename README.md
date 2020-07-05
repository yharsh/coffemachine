# Coffee Machine
Provide simultaneous serving of multiple beverages whose number can be set using init param file along with ingredients and recipes supported.

## Supported Operations:
- Add ingredients(blocking command)
- Add recipe(blocking command)
- Prepare Beverage(can be executed in parallel but at max count_n outlet will be serving, when already count_n are being served then will reject futher request until unless one outlet is available)

## How to build
`mvn clean install`

## How to run:
`java -jar <path to jar file> --initFile=<path to init file> --commandFile=<path to command file>`

`java -jar target/coffeemachine-0.0.1-SNAPSHOT.jar --initFile=src/main/resources/init.json --commandFile=src/main/resources/command.json`

## Notes:
- src/main/resources/init.json can be used as init file
- src/main/resources/command.json can be used as command file
- src/main/resources/sampleExecutionFile.log is one generated during local run
