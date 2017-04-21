import { Injectable } from '@angular/core';
import { Http, Response, URLSearchParams, BaseRequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Wish } from './wish.model';
import { DateUtils } from 'ng-jhipster';
@Injectable()
export class WishService {

    private resourceUrl = 'api/wishes';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(wish: Wish): Observable<Wish> {
        let copy: Wish = Object.assign({}, wish);
        copy.addedOn = this.dateUtils
            .convertLocalDateToServer(wish.addedOn);
        copy.completeBy = this.dateUtils.toDate(wish.completeBy);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(wish: Wish): Observable<Wish> {
        let copy: Wish = Object.assign({}, wish);
        copy.addedOn = this.dateUtils
            .convertLocalDateToServer(wish.addedOn);

        copy.completeBy = this.dateUtils.toDate(wish.completeBy);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Wish> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            let jsonResponse = res.json();
            jsonResponse.addedOn = this.dateUtils
                .convertLocalDateFromServer(jsonResponse.addedOn);
            jsonResponse.completeBy = this.dateUtils
                .convertDateTimeFromServer(jsonResponse.completeBy);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<Response> {
        let options = this.createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: any) => this.convertResponse(res))
        ;
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }


    private convertResponse(res: any): any {
        let jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            jsonResponse[i].addedOn = this.dateUtils
                .convertLocalDateFromServer(jsonResponse[i].addedOn);
            jsonResponse[i].completeBy = this.dateUtils
                .convertDateTimeFromServer(jsonResponse[i].completeBy);
        }
        res._body = jsonResponse;
        return res;
    }

    private createRequestOption(req?: any): BaseRequestOptions {
        let options: BaseRequestOptions = new BaseRequestOptions();
        if (req) {
            let params: URLSearchParams = new URLSearchParams();
            params.set('page', req.page);
            params.set('size', req.size);
            if (req.sort) {
                params.paramsMap.set('sort', req.sort);
            }
            params.set('query', req.query);

            options.search = params;
        }
        return options;
    }
}
