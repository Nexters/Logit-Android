package constants

private const val MAJOR_VERSION = 1
private const val MINOR_VERSION = 0
private const val PATCH_VERSION = 2
internal const val VERSION_CODE = (MAJOR_VERSION * 10000) + (MINOR_VERSION * 100) + PATCH_VERSION
internal const val VERSION_NAME = "$MAJOR_VERSION.$MINOR_VERSION.$PATCH_VERSION"
