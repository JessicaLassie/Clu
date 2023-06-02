# Clu

## Goals

Clu is a desktop application developed in Java/Swing allowing to generate DTO.

## Changelog

Cf CHANGELOG.md

## Current OSS / COTS dependencies

| Name                                                             | Description                                                            | Licence                                                            | Usage   |
| ---------------------------------------------------------------- | ---------------------------------------------------------------------- | ------------------------------------------------------------------ | ------- |
| [FlatLaf](https://www.formdev.com/flatlaf/)                      | Look and Feel                                                          | [Apache-2.0](https://www.formdev.com/flatlaf/#license)             | Compile |
| [MySQL Connector](https://dev.mysql.com/doc/connector-j/8.0/en/) | Driver for communicating with MySQL servers                            | [GPLv2](https://dev.mysql.com/doc/connector-j/8.0/en/preface.html) | Compile |
| [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/)           | Driver which communicates using the PostgreSQL native network protocol | [BSD-2-Clause](https://jdbc.postgresql.org/license/)               | Compile |
| [Apache Log4j 2](https://logging.apache.org/log4j/2.x/)          | Logging tool                                                           | [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0)          | Compile |

## Executables environment

### Executables and environment variables

At least Java 8

## Developpers

### App build
`mvn package`

### App launch
`java -jar clu-<version>.jar`
