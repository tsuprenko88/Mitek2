// Hello.m
#import <Foundation/Foundation.h>

@interface Hello: NSObject
@property (nonatomic) NSString* s;
@end

@implementation Hello
    
- (void)setS:(NSString)s {
    _s = @"";
}

- (NSString *)getS {
    return @"get s";
}
    
@end
