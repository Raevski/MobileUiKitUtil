# Mobile UiKit Util

## Motivaiton

Advanced Command line utility to export colors, typography, icons Figma API to Android Studio or Xcode project
The advanced feature of this tool is support of tokenization

- TYPOGRAPHY("Typography") - for exporting Typography styles
- COLORS("Colors") - for exporting colors
- ICONS("Icons") - for exporting icons

What purpose of this instrument

Figma doesn't support exporting typography, colors and images to Xcode / Android Studio. Manual export takes a long time.
For easy sync of ui kit with the code and making merge requests with uikit updaed in ci you need
command line tool.

## Features

- [x] Reading config for util from dsl file and parsing it for model params
- [x] Generating Android JetPack Compose typography class for Android Project 
- [ ] Generating Android JetPack Compose colors class for Android Project
- [ ] Generating Android JetPack Compose icons class for Android Project
- [ ] Generating IOS SWIFTUI typography class for Xcode Project
- [ ] Generating IOS SWIFTUI colors class for Xcode Project
- [ ] Generating IOS SWIFTUI icons class for Xcode Project
- [ ] Generating Android Xml typography file for Android Project
- [ ] Generating Android Xml colors file for Android Project
- [ ] Generating Android icons files for Android Project
- [ ] Generating IOS typography file for XCode Project
- [ ] Generating IOS colors file for XCode Project
- [ ] Generating IOS icons files for XCode Project
- [ ] Support for tokenization for styles, colors, icons names

## Config

Copy mobile_figma_util_config.muc.template to jar directory, remove .temlate from the file name, 
and enter your preferences

### Config sample
```
MobileFigmaExportConfig {
    figmaToken = "your figma api token"
    fileHash = "your figma file id hash"
    platform = "android"
    resourceType = "Typography"
    isLogging = true
    resultPath = "src/main/res"
    tokenization {
        "body_3" to "bodyMain"
    }
}
```
## How to run util
java -jar build/libs/MobileUIKitUtil-1.0.1-SNAPSHOT.jar mobile_figma_util_config.muc