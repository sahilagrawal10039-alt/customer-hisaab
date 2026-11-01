CUSTOMER HISAAB ANDROID APP

This project wraps the V12 website in an Android WebView and stores browser localStorage on the device.
The website is bundled in app/src/main/assets/index.html, so core entry/history works offline.
Printing is configured for actual A5 landscape paper through Android PrintManager.

GITHUB UPLOAD:
Upload the CONTENTS of this folder into the root of your existing customer-hisaab repository, merging with the existing website files. Do not delete the website files.

BUILD APK:
After committing, open repository > Actions > Build Customer Hisaab APK > Run workflow.
When green, open the run > Artifacts > Customer-Hisaab-APK and download app-debug.apk.
Install the APK on your Android phone. Allow installation from this source if Android asks.

Note: Share Photo/PDF uses the website's existing html2canvas CDN loader; those export/share features need internet the first time the library is fetched. Core entry and saved history are local/offline.
