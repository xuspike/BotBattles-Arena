#! /bin/bash

scp ./backendcloud/backend/target/backend-0.0.1-SNAPSHOT.jar spike:kob/backendcloud/

scp ./backendcloud/matchingsystem/target/matchingsystem-0.0.1-SNAPSHOT.jar spike:kob/backendcloud/

scp ./backendcloud/botrunningsystem/target/botrunningsystem-0.0.1-SNAPSHOT.jar spike:kob/backendcloud/

scp -r ./web/dist/ spike:kob/web/
