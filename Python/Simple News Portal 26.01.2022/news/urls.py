from django.urls import path
from . import views

urlpatterns = [
    path('', views.ComingSoon.as_view()),
    path('news/', views.MainPage.as_view(), name='main'),
    path('news/<int:link>/', views.News.as_view()),
    path('news/create/', views.CreateNews.as_view())
]