aws cognito-idp sign-up \
  --client-id 4su8uq5p0imfmoa364r301ejmu \
  --username 'duke' \
  --password 'Duke!1duke'

aws cognito-idp admin-confirm-sign-up \
  --user-pool-id eu-central-1_ryhv7vbr0 \
  --username 'abien'

aws cognito-idp initiate-auth \
    --client-id 4su8uq5p0imfmoa364r301ejmu \
    --auth-flow USER_PASSWORD_AUTH \
    --auth-parameters USERNAME=duke,PASSWORD='Duke!1duke' \
    --output json


curl -H"Authorization:  eyJraWQiOiJEazBwWnhRQ09IK0pocEM5K0ZxTVVtQmoyaWZyemhzSHZiSGQzSGN4ZWo4PSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiIzYmJlNjdmMC1kMmMyLTRhZjktOGViYi03MGNmMDU4MTIzMjMiLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuZXUtY2VudHJhbC0xLmFtYXpvbmF3cy5jb21cL2V1LWNlbnRyYWwtMV9yeWh2N3ZicjAiLCJjbGllbnRfaWQiOiI0c3U4dXE1cDBpbWZtb2EzNjRyMzAxZWptdSIsIm9yaWdpbl9qdGkiOiIwNWZiM2UyZS01OTM2LTQ3NTktYjIwYS1jZTUyMjBiNmYzM2UiLCJldmVudF9pZCI6ImU0MTUyYjcwLWU5NmYtNGU0Mi04NDU0LTM0ZDVhNzU4NzIzMyIsInRva2VuX3VzZSI6ImFjY2VzcyIsInNjb3BlIjoiYXdzLmNvZ25pdG8uc2lnbmluLnVzZXIuYWRtaW4iLCJhdXRoX3RpbWUiOjE2ODY4Mzk3NDUsImV4cCI6MTY4Njg0MzM0NSwiaWF0IjoxNjg2ODM5NzQ1LCJqdGkiOiIyNmQ1NjAxZi04N2VlLTQ0NTgtYTkxNC0zOWM1NTBlM2FmODMiLCJ1c2VybmFtZSI6ImR1a2UifQ.BxVuwQEWyHljF4PS3CU6DbOlsxubiFJZJuOQr7b3RFbqMvBPza2fcFtrTRCl6IcvPXzyaTVsJrmW_NF2NHr7gU-MWIwdGmWHI-D8Pk5nC0OzJSA6-IGqLK3869G3ri17IOxtdgbFJDedy0ehPF_fqrtD554xZGG7TBHva4GzevnuBUVOIWph0U_IONLYeTATeu9oQZLpa7YP2AfPSjpJeCJYG9-AE4zcTdzdOSX4LzKZT5V9EcFG6ePIOcM1tWDRtaL_i0njdw1bLpbqs2XbpFez1SLxTnh0F9KSy8vNJ9Zf-JB0VbOP3079vCrVHRHPBkXksgsVWnyHSAe52gpI9A" https://lambda-quarkus-cognito.airhacks.net/hello