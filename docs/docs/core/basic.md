# Quick Start

## Display Image

Just use it like this for display image:

```kotlin
// Option 1 on 1.7.0+
AutoSizeImage(
    "https://...",
    contentDescription = "image",
)
// Option 2 on 1.7.0+
AutoSizeBox("https://...") { action ->
    when (action) {
        is ImageAction.Success -> {
            Image(
                rememberImageSuccessPainter(action),
                contentDescription = "image",
            )
        }
        is ImageAction.Loading -> {}
        is ImageAction.Failure -> {}
    }
}
// Option 3
Image(
    painter = rememberImagePainter("https://.."),
    contentDescription = "image",
)
```

Use priority: `AutoSizeImage` -> `AutoSizeBox` -> `rememberImagePainter`.

`AutoSizeBox` & `AutoSizeImage` are based on **Modifier.Node**, `AutoSizeImage` ≈ `AutoSizeBox` + `Painter`.

PS: default `Imageloader` will reload when it's displayed, is not friendly for `https` link, so it is recommended to custom `ImageLoader` and configure the cache.

## Custom ImageLoader

I configure the `Imageloader {}` on each platform, you also can configure it in the `commonMain` like [Tachidesk-JUI](https://github.com/Suwayomi/Tachidesk-JUI/blob/master/presentation/src/commonMain/kotlin/ca/gosyer/jui/ui/base/image/ImageLoaderProvider.kt).

```kotlin
@Composable
fun Content() {
    CompositionLocalProvider(
        LocalImageLoader provides remember { generateImageLoader() },
    ) {
        // App
    }
}
```

#### in Android

```kotlin title="MainActivity.kt"
fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        options {
            androidContext(context)
        }
        components {
            setupDefaultComponents()
        }
        interceptor {
            // cache 100 success image result, without bitmap
            defaultImageResultMemoryCache()
            memoryCacheConfig {
                // Set the max size to 25% of the app's available memory.
                maxSizePercent(context, 0.25)
            }
            diskCacheConfig {
                directory(context.cacheDir.resolve("image_cache").toOkioPath())
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}
```

#### in Jvm

```kotlin
fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        components {
            setupDefaultComponents()
        }
        interceptor {
            // cache 100 success image result, without bitmap
            defaultImageResultMemoryCache()
            memoryCacheConfig {
                maxSizeBytes(32 * 1024 * 1024) // 32MB
            }
            diskCacheConfig {
                directory(getCacheDir().toOkioPath().resolve("image_cache"))
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}

// about currentOperatingSystem, see app
private fun getCacheDir() = when (currentOperatingSystem) {
    OperatingSystem.Windows -> File(System.getenv("AppData"), "$ApplicationName/cache")
    OperatingSystem.Linux -> File(System.getProperty("user.home"), ".cache/$ApplicationName")
    OperatingSystem.MacOS -> File(System.getProperty("user.home"), "Library/Caches/$ApplicationName")
    else -> throw IllegalStateException("Unsupported operating system")
}
```

#### in iOS & Macos

```kotlin
fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        components {
            setupDefaultComponents()
        }
        interceptor {
            // cache 100 success image result, without bitmap
            defaultImageResultMemoryCache()
            memoryCacheConfig {
                maxSizeBytes(32 * 1024 * 1024) // 32MB
            }
            diskCacheConfig {
                directory(getCacheDir().toPath().resolve("image_cache"))
                maxSizeBytes(512L * 1024 * 1024) // 512MB
            }
        }
    }
}

private fun getCacheDir(): String {
    return NSSearchPathForDirectoriesInDomains(
        NSCachesDirectory,
        NSUserDomainMask,
        true,
    ).first() as String
}

```

#### in Js

```kotlin
fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        // ...
        interceptor {
             memoryCacheConfig {
                maxSizeBytes(32 * 1024 * 1024) // 32MB
            }
            // At the moment I don't know how to configure the disk cache in js either
            diskCacheConfig(FakeFileSystem().apply { emulateUnix() }) {
                directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY)
                maxSizeBytes(256L * 1024 * 1024) // 256MB
            }
        }
    }
}
```
