# Personal AppLock (starter MVP)

[![Download Latest APK](https://img.shields.io/badge/Download-Latest_APK-green?style=for-the-badge&logo=android)](https://github.com/PKBEAST1402/PersonalAppLock/releases/latest)

A beginner-friendly Android Studio starter for a personal app locker using Kotlin.
It includes app selection, PIN, biometric prompt, and an AccessibilityService-based
foreground-app monitor.

## Important limitations
- This is a starter project, not a security-audited product. Test on a spare device first.
- Android/OEM background-activity rules vary. The monitor may not reliably block every app on every device.
- The current implementation uses a temporary 30-second unlock window and is not tamper-proof.
- Intruder selfie and launcher-icon hiding are intentionally not implemented in this MVP. They need additional camera/launcher handling and careful permission/recovery UX.
- Do not use this to monitor another person's device. Accessibility access is powerful; enable it only for your own phone.
- The default PIN is `1234`. Change it immediately before testing.
- Installed-app visibility is broad and may require Play policy declarations if ever distributed publicly. This private sideload build is for personal testing.

## Open and build
1. Install Android Studio (current stable) and its Android SDK.
2. Open this folder as a project.
3. Let Gradle sync. If Android Studio suggests a compatible Gradle/AGP update, accept it.
4. Build > Build Bundle(s) / APK(s) > Build APK(s).
5. Find the APK under `app/build/outputs/apk/debug/app-debug.apk`.

## First run
1. Install the APK.
2. Open Personal AppLock and change the PIN.
3. Select apps to lock.
4. Tap **Enable AppLock monitoring**.
5. In Android Accessibility settings, enable **Personal AppLock monitor**.
6. Test with a non-critical app first. Disable the accessibility service if you need to troubleshoot.

## Private GitHub workflow
Create a **private** repository on GitHub. Do not commit signing keys, passwords, or personal data.
```bash
git init
git add .
git commit -m "Initial Personal AppLock MVP"
git branch -M main
git remote add origin https://github.com/YOUR-USERNAME/YOUR-PRIVATE-REPO.git
git push -u origin main
```
To update later:
```bash
git add .
git commit -m "Describe your change"
git push
```
GitHub stores source code; it does not build/install the APK automatically unless you add a CI workflow.
