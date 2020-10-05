a = imread('C:\Users\Surya Reddy\Desktop\ImageProcessing-Matlab\Test\test1.jpg');
 
figure;
imshow(a)
matlabroot=('C:\Users\Surya Reddy\Desktop\ImageProcessing-Matlab\Train');
Datasetpath=fullfile(matlabroot,'cnn2','dataset');
imds = imageDatastore(fullfile(matlabroot),'IncludeSubfolders',true,'FileExtensions','.jpg','LabelSource','foldernames');

 
layers = [imageInputLayer([515 415  3])
convolution2dLayer(5,20)
reluLayer
maxPooling2dLayer(2,'stride',2)
convolution2dLayer(5,20)
reluLayer 
maxPooling2dLayer(2,'stride',2)
fullyConnectedLayer(2)
softmaxLayer
classificationLayer()]

options=trainingOptions('sgdm','MiniBatchSize',10,'MaxEpochs',10,'initialLearnrate',0.001');

net= trainNetwork(imds,layers,options);
save net;

load 'net.mat';

output = classify(net,a);

disp(output);