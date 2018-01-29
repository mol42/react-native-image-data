
#import "RNReactNativeImageData.h"
#import "UIImage+ColorAtPixel.h"
#import <React/RCTImageLoader.h>

@implementation RNReactNativeImageData

@synthesize bridge = _bridge;

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()

RCT_REMAP_METHOD(getSimpleGrayscalePixels,
                    path :(NSString *)path
                    options:(NSDictionary *)options
                    findEventsWithResolver:(RCTPromiseResolveBlock)resolve
                    rejecter:(RCTPromiseRejectBlock)reject)
{
    NSURL *imageUrl = [[NSURL alloc] initWithString:path];
    UIImage *image = [UIImage imageWithData:[NSData dataWithContentsOfURL:imageUrl]];

    if (image == nil) {
        NSError *error = [NSError errorWithDomain:@"com.mol42" code:500 userInfo:@{@"Error reason": @"Image could not be read"}];
        reject(@"no_events", @"There were no events", error);
        return;
    }

    NSNumber *threshold = [NSNumber numberWithDouble:0.5];
    NSInteger maxWidth = [RCTConvert NSInteger:options[@"maxWidth"]];
    NSInteger maxHeight = [RCTConvert NSInteger:options[@"maxHeight"]];
    CGSize newSize = CGSizeMake(maxWidth, maxHeight);
    UIImage *scaledImage = [self scaleImage:image toSize:newSize];
    NSMutableArray *pixels = [NSMutableArray array];
    
    for (int x = 0; x < maxWidth; x++) {
        for (int y = 0; y < maxHeight; y++) {
            CGPoint point = CGPointMake(x, y);
            UIColor *pixelColor = [scaledImage colorAtPixel:point];
            CGFloat *components = CGColorGetComponents(pixelColor.CGColor);
            CGFloat r = components[0];
            CGFloat g = components[1];
            CGFloat b = components[2];
            NSNumber *luminanceFloat = [NSNumber numberWithDouble:((0.299 * r + 0.587 * g + 0.114 * b))];
            // NSInteger luminance = [luminanceFloat intValue];
            NSString *pixelString = [luminanceFloat doubleValue] < [threshold doubleValue] ? @"1" : @"0";
            [pixels addObject:pixelString];
        }
    }

    resolve(pixels);
}

NSString * hexStringForColor( UIColor* color ) {
    const CGFloat *components = CGColorGetComponents(color.CGColor);
    CGFloat r = components[0];
    CGFloat g = components[1];
    CGFloat b = components[2];
    NSString *hexString=[NSString stringWithFormat:@"#%02X%02X%02X", (int)(r * 255), (int)(g * 255), (int)(b * 255)];

    return hexString;
}

- (UIImage *)scaleImage:(UIImage *)originalImage toSize:(CGSize)size
{       
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    CGContextRef context = CGBitmapContextCreate(NULL, size.width, size.height, 8, 0, colorSpace, kCGImageAlphaPremultipliedLast);
    CGContextClearRect(context, CGRectMake(0, 0, size.width, size.height));
    
    if (originalImage.imageOrientation == UIImageOrientationRight) {
        CGContextRotateCTM(context, -M_PI_2);
        CGContextTranslateCTM(context, -size.height, 0.0f);
        CGContextDrawImage(context, CGRectMake(0, 0, size.height, size.width), originalImage.CGImage);
    } else {
        CGContextDrawImage(context, CGRectMake(0, 0, size.width, size.height), originalImage.CGImage);
    }
        
    CGImageRef scaledImage = CGBitmapContextCreateImage(context);
    CGColorSpaceRelease(colorSpace);
    CGContextRelease(context);
    
    UIImage *image = [UIImage imageWithCGImage:scaledImage];
    CGImageRelease(scaledImage);
    
    return image;
}

@end
  