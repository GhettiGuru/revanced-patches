## 🧩 ReVanced Patches

ReVanced Extended Patches.

## Documentation

Check the [wiki](https://github.com/anddea/revanced-patches/wiki) for resources on patching, customization, and debugging.

## 📋 List of patches in this repository

{{ table }}

## 📝 JSON Format

This section explains the JSON format for the [patches.json](patches.json) file.

Example:

```json
[
  {
    "name": "Alternative thumbnails",
    "description": "Adds options to replace video thumbnails using the DeArrow API or image captures from the video.",
    "use":true,
    "compatiblePackages": {
      "com.google.android.youtube": "COMPATIBLE_PACKAGE_YOUTUBE"
    },
    "options": []
  },
  {
    "name": "Bitrate default value",
    "description": "Sets the audio quality to 'Always High' when you first install the app.",
    "use":true,
    "compatiblePackages": {
      "com.google.android.apps.youtube.music": "COMPATIBLE_PACKAGE_MUSIC"
    },
    "options": []
  },
  {
    "name": "Hide ads",
    "description": "Adds options to hide ads.",
    "use":true,
    "compatiblePackages": {
      "com.reddit.frontpage": "COMPATIBLE_PACKAGE_REDDIT"
    },
    "options": []
  }
]
```