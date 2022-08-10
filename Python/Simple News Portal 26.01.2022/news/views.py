from django.shortcuts import render
from django.views import View
from django.conf import settings
from django.shortcuts import redirect, Http404
from datetime import date
import json


class ComingSoon(View):
    def get(self, request):
        return redirect('main')


class MainPage(View):
    def _context(self, link):
        news = dict()
        with open(settings.NEWS_JSON_PATH, 'r') as f:
            data = json.load(f)
            if link is not None:
                data = filter(lambda x: x['link'] == int(link), data)
            for property in data:
                created_date = property['created'].split()[0]
                if created_date not in news.keys():
                    news.setdefault(created_date, list())
                news[created_date].append(
                    {'link': f'target="_blank" href="/news/{property["link"]}/"', 'title': property['title']}
                )

            return [{'created': k, 'properties': v} for k, v in
                    sorted(news.items(), key=lambda x: x[0], reverse=True)]

    def post(self, request):
        return render(request, 'index.html', context={'news': self._context(request.POST.get('q'))})

    def get(self, request):
        return render(request, 'index.html', context={'news': self._context(request.GET.get('q'))})


class News(View):
    def get(self, request, link):
        with open(settings.NEWS_JSON_PATH, 'r') as f:
            for j in json.load(f):
                if j['link'] == link:
                    context = {"title": j['title'], "created": j['created'], 'text': j['text']}
                    return render(request, 'news.html', context=context)
        raise Http404


class CreateNews(View):
    def get(self, request):
        return render(request, 'create.html')

    def post(self, request):
        with open(settings.NEWS_JSON_PATH, 'r') as f:
            data = json.load(f)

        entity = {
            'created': date.strftime(date.today(), '%Y-%m-%d %H:%M:%S'),
            'text': request.POST.get('text'),
            'title': request.POST.get('title'),
            'link': max(map(lambda x: x['link'], data)) + 1
        }

        with open(settings.NEWS_JSON_PATH, 'w') as f:
            data.append(entity)
            f.write(json.dumps(data))

        return redirect('main')
