CUSTOMER HISAAB ANDROID APP — V2

This is a complete Android Studio/Gradle project. It wraps the V12 Customer Hisaab website in an Android WebView.
The website is bundled at app/src/main/assets/index.html, so customer entry and saved history remain on-device and core use works offline.
Android printing is configured for physical A5 landscape paper.

UPLOAD TO YOUR EXISTING GITHUB REPOSITORY:
1. Extract this ZIP.
2. Upload the CONTENTS of the extracted folder to the ROOT of the existing customer-hisaab repository.
3. Choose the option to replace the existing .github/workflows/build-apk.yml if GitHub asks.
4. Keep the existing root website index.html; do not delete it. The Android app uses its own bundled copy at app/src/main/assets/index.html.
5. Commit the upload.

BUILD:
1. Open repository > Actions > Build Customer Hisaab APK.
2. Select Run workflow if it does not start automatically.
3. Wait for the green successful run.
4. Open the run and download the Customer-Hisaab-APK artifact.
5. Extract the artifact ZIP to get app-debug.apk and install it on the Android phone.

IMPORTANT:
- This package includes the complete Gradle project and a corrected GitHub Actions workflow.
- The workflow deliberately does not use android-actions/setup-android, which caused the previous "Failed to find package 'tools'" error.
- Share Photo/PDF in the current website may fetch html2canvas from a CDN; that export helper needs internet on first fetch. Normal entry and local history are bundled/local.
