
import { NativeModules, Platform } from 'react-native';

const { RNReactNativeImageData } = NativeModules;

const ReactNativeImageData = {

    getSimpleGrayscalePixels : (imagePath, {scaledWidth, scaledHeight}) => {
        
        if (Platform.OS == "ios") {
            return RNReactNativeImageData.getSimpleGrayscalePixels(imagePath, {scaledWidth, scaledHeight});
        } else {
            return RNReactNativeImageData.getSimpleGrayscalePixels(imagePath, scaledWidth, scaledHeight);
        }
    },

    getSimpleGrayscalePixelsAsXYMatrix : (imagePath, {scaledWidth, scaledHeight}) => {
        
        let imagePixelsArray = RNReactNativeImageDataWrapper.getSimpleGrayscalePixels(imagePath, {scaledWidth, scaledHeight});
        let imageXYMatrix = [];

        for (let i = 0; i < scaledWidth; i++) {
            imageXYMatrix[i] = [];
        }

        let index = 0;

        for (let i = 0; i < scaledWidth; i++) {
            for (let j = 0; j < scaledHeight; j++) {
                imageXYMatrix[j][i] = imagePixelsArray[index++];
            }
        }

        return imageXYMatrix;
    }
}

export default {ReactNativeImageData};
