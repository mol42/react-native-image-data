
Pod::Spec.new do |s|
  s.name         = "RNReactNativeImageData"
  s.version      = "1.0.0"
  s.summary      = "RNReactNativeImageData"
  s.description  = <<-DESC
                  RNReactNativeImageData
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "tayfun@mol42.com" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/mol42/react-native-image-data.git", :tag => "master" }
  s.source_files  = "RNReactNativeImageData/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  