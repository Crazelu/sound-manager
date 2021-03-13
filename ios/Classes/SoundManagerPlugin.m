#import "SoundManagerPlugin.h"
#if __has_include(<sound_manager/sound_manager-Swift.h>)
#import <sound_manager/sound_manager-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "sound_manager-Swift.h"
#endif

@implementation SoundManagerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftSoundManagerPlugin registerWithRegistrar:registrar];
}
@end
