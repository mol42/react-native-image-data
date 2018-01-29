
import { NativeModules, Platform } from 'react-native';

const { RNReactNativeImageData } = NativeModules;

const RNReactNativeImageDataWrapper = {

    getSimpleGrayscalePixels : (imagePath, {scaledWidth, scaledHeight}) => {
        
        if (Platform.OS == "ios") {
            return RNReactNativeImageData.getSimpleGrayscalePixels(imagePath, {scaledWidth, scaledHeight});
        } else {
            return RNReactNativeImageData.getSimpleGrayscalePixels(imagePath, scaledWidth, scaledHeight);
        }
    }
}

export default RNReactNativeImageDataWrapper;
