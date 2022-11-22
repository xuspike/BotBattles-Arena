scp dist/js/*.js Kob:kob/acapp/
scp dist/css/*.css Kob:kob/acapp/

ssh Kob 'cd kob/acapp && ./rename.sh'
