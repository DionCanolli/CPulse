import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InterceptorService implements HttpInterceptor {

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      if (!request.url.startsWith('http://localhost:8080/permitted')) {
          const token = localStorage.getItem('jwtToken');
          if (token) {
              request = request.clone({
                  setHeaders: {
                      Authorization: token
                  }
              });
          }
      }
      return next.handle(request);
  }
}

/*
   request: HttpRequest<any>: This represents the outgoing HTTP request that is being intercepted.
   next: HttpHandler: This is a handler that allows the request to be passed to the next interceptor
         in the chain or to the backend if there are no more interceptors
   Return Type: The method returns an Observable<HttpEvent<any>>, which represents the response from
                the HTTP request.
*/