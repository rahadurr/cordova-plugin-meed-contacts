#import <Cordova/CDV.h>
#import "CVDMeedContact.h"

@interface CVDMeedContacts : CDVPlugin {
  // Member variables go here.
}

- (void)all:(CDVInvokedUrlCommand*)command;
- (void)search:(CDVInvokedUrlCommand*)command;
@end
