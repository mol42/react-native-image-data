
import { NativeModules, Platform } from 'react-native';

const { RNReactNativeImageData } = NativeModules;

let RNReactNativeImageDataWrapper = {

    getSimpleGrayscalePixels : (imagePath, {scaledWith, scaledHeight}) => {
        
        if (Platform.OS == "ios") {
            return RNReactNativeImageData.getSimpleGrayscalePixels(imagePath, {scaledWith, scaledHeight});
        } else {
            return RNReactNativeImageData.getSimpleGrayscalePixels(imagePath, scaledWith, scaledHeight);
        }
    }
}

export default RNReactNativeImageDataWrapper;
