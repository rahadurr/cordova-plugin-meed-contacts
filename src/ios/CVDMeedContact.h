#import <Foundation/Foundation.h>
#import <Contacts/Contacts.h>


@interface CVDMeedContact : NSObject {
    // Member variables go here.
}

- (NSArray*)all:(NSDictionary*)command;
- (NSArray*)search:(NSString*)searchString searchOptios:(NSDictionary*)options;
@end
