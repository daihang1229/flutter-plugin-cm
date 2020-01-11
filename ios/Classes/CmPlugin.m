#import "CmPlugin.h"
#import <cm/cm-Swift.h>

@implementation CmPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftCmPlugin registerWithRegistrar:registrar];
}
@end
