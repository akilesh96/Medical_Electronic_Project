from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from django.core.files.storage import FileSystemStorage
from matlab_engine.run import execute
import os

# Create your views here.
class FindCells(APIView):
    def post(self, request):
        img_file = request.FILES.get("image_file", False)
        store_file = FileSystemStorage()
        file_name = store_file.save(img_file.name, img_file)
        # file_path = store_file.path(file_name)
        # folder_name = file_name.split("/")[-1].split(".")[-2]
        # folder_path = store_file.base_location+"/"+folder_name
        # os.mkdir(folder_path)
        cells = execute("images/"+file_name)
        if cells:
            ret = cells
        # output_images = ["output.png", "black_and_white.png", "dilated.png", "HSV.png", "holes_filled2.png"]
        # return Response({"error":False, "cells_count": cells, "images": [request.get_host()+"/"+folder_path+"/"+i for i in output_images]})
        return Response({"error": False, "label": ret})